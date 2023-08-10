package com.mindex.challenge.service.impl;

import com.mindex.challenge.dao.CompensationRepository;
import com.mindex.challenge.dao.EmployeeRepository;
import com.mindex.challenge.data.Compensation;
import com.mindex.challenge.data.Employee;
import com.mindex.challenge.exception.CompensationAlreadyExistsException;
import com.mindex.challenge.exception.EmployeeDoesNotExistException;
import com.mindex.challenge.service.CompensationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CompensationServiceImpl implements CompensationService {

    @Autowired
    private CompensationRepository compensationRepository;

    @Autowired
    private EmployeeRepository employeeRepository;
    @Override
    public Compensation createCompensation(String employeeId, Compensation compensation) {

        if(employeeRepository.findByEmployeeId(employeeId) == null){
            throw new EmployeeDoesNotExistException();
        }
        // In a real world scenario, I would want to get requirement clarification here.
        // Does it support multiple compensations OR is this an update?
        if(compensationRepository.findByEmployeeEmployeeId(employeeId) != null){
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
        Employee employee = employeeRepository.findByEmployeeId(employeeId);
        if(employee == null){
            throw new EmployeeDoesNotExistException();
        }


        Compensation compensation = compensationRepository.findByEmployeeEmployeeId(employeeId);
        if(compensation == null){
            return Optional.empty();
        } else {
            compensation.setEmployee(employee);
            return Optional.of(compensation);
        }
    }
}
