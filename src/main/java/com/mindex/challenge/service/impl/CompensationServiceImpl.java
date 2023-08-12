package com.mindex.challenge.service.impl;

import com.mindex.challenge.dao.CompensationRepository;
import com.mindex.challenge.dao.EmployeeRepository;
import com.mindex.challenge.data.Compensation;
import com.mindex.challenge.data.Employee;
import com.mindex.challenge.error.exception.CompensationAlreadyExistsException;
import com.mindex.challenge.error.exception.EmployeeDoesNotExistException;
import com.mindex.challenge.service.CompensationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class CompensationServiceImpl implements CompensationService {

    private static final Logger LOG = LoggerFactory.getLogger(CompensationServiceImpl.class);

    @Autowired
    private CompensationRepository compensationRepository;

    @Autowired
    private EmployeeRepository employeeRepository;
    @Override
    public Compensation createCompensation(String employeeId, Compensation compensation) {
        LOG.debug("Creating new compensation for employee id [{}]", employeeId);
        if(employeeRepository.findByEmployeeId(employeeId) == null){
            LOG.error("Employee for id [{}] does not exist", employeeId);
            throw new EmployeeDoesNotExistException();
        }
        // The effective_date in the compensation object implies to me that salary can change over time and that we would want to maintain a historical record,
        if(compensationRepository.findByEmployeeEmployeeIdAndEffectiveDate(employeeId, compensation.getEffectiveDate()).isPresent()){
            LOG.error("Compensation for id [{}] already exists and cannot be recreated", employeeId);
            throw new CompensationAlreadyExistsException();
        }

        // only store the employeeID so that if employee data changes over time, it only needs to be updated in one place
        Employee compensationEmployee = new Employee();
        compensationEmployee.setEmployeeId(employeeId);
        compensation.setEmployee(compensationEmployee);
        return compensationRepository.insert(compensation);
    }

    @Override
    public Optional<Compensation> readCompensation(String employeeId){
        LOG.debug("Reading compensation for employee id [{}]", employeeId);
        Employee employee = employeeRepository.findByEmployeeId(employeeId);
        if(employee == null){
            LOG.error("Employee for id [{}] does not exist", employeeId);
            throw new EmployeeDoesNotExistException();
        }


        List<Compensation> compensationResults = compensationRepository.findByEmployeeEmployeeIdAndEffectiveDateLessThanOrderByEffectiveDateDesc(employeeId, LocalDateTime.now());
        if(compensationResults.isEmpty()){
            LOG.debug("No compensation found for employee id [{}]", employeeId);
            return Optional.empty();
        } else {
            LOG.debug("Compensation found for employee id [{}]:", employeeId);
            Compensation compensation = compensationResults.get(0);
            compensation.setEmployee(employee);
            return Optional.of(compensation);
        }
    }
}
