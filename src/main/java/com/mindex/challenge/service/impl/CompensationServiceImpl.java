package com.mindex.challenge.service.impl;

import com.mindex.challenge.dao.CompensationRepository;
import com.mindex.challenge.dao.EmployeeRepository;
import com.mindex.challenge.data.Compensation;
import com.mindex.challenge.data.Employee;
import com.mindex.challenge.exception.CompensationAlreadyExistsException;
import com.mindex.challenge.exception.EmployeeDoesNotExistException;
import com.mindex.challenge.service.CompensationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
        // In a real world scenario, I would want to get requirement clarification here.
        // Does it support multiple compensations OR is this an update?
        if(compensationRepository.findByEmployeeEmployeeId(employeeId) != null){
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


        Compensation compensation = compensationRepository.findByEmployeeEmployeeId(employeeId);
        if(compensation == null){
            LOG.debug("No compensation found for employee id [{}]", employeeId);
            return Optional.empty();
        } else {
            LOG.debug("Compensation found for employee id [{}]:", employeeId);
            compensation.setEmployee(employee);
            return Optional.of(compensation);
        }
    }
}
