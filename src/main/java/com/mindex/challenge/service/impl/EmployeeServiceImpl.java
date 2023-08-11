package com.mindex.challenge.service.impl;

import com.mindex.challenge.dao.EmployeeRepository;
import com.mindex.challenge.data.Employee;
import com.mindex.challenge.exception.EmployeeDoesNotExistException;
import com.mindex.challenge.response.ReportingStructure;
import com.mindex.challenge.service.EmployeeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    private static final Logger LOG = LoggerFactory.getLogger(EmployeeServiceImpl.class);

    @Autowired
    private EmployeeRepository employeeRepository;

    @Override
    public Employee create(Employee employee) {
        LOG.debug("Creating employee [{}]", employee);

        employee.setEmployeeId(UUID.randomUUID().toString());
        employeeRepository.insert(employee);

        return employee;
    }

    @Override
    public Employee read(String id) {
        LOG.debug("Creating employee with id [{}]", id);

        Employee employee = employeeRepository.findByEmployeeId(id);

        if (employee == null) {
            throw new RuntimeException("Invalid employeeId: " + id);
        }

        return employee;
    }

    @Override
    public Employee update(Employee employee) {
        LOG.debug("Updating employee [{}]", employee);

        return employeeRepository.save(employee);
    }

    @Override
    public ReportingStructure report(String id) {
        LOG.debug("Creating report for employee id [{}]", id);
        // get the employee
        Employee employee = employeeRepository.findByEmployeeId(id);
        if(employee == null){
            throw new EmployeeDoesNotExistException("Invalid employee id "+id);
        }
        // iterate through the tree of reports using queue
        LinkedList<Employee> queue = new LinkedList<>(getDirectReports(employee));
        int numberOfReports = 0;
        while(!queue.isEmpty()){
            numberOfReports++;
            Employee currentEmployee = employeeRepository.findByEmployeeId(queue.pop().getEmployeeId());
            if(currentEmployee != null) {
                queue.addAll(getDirectReports(currentEmployee));
            }
        }
        ReportingStructure report = new ReportingStructure();
        report.setEmployee(employee);
        report.setNumberOfReports(numberOfReports);
        return report;
    }


    /**
     * Gets direct reports from employee without returning null.
     * Avoids updating the POJO with this behavior and changing the return values of other endpoints.
     */
    private List<Employee> getDirectReports(Employee employee){
        return employee.getDirectReports() != null ? employee.getDirectReports() : Collections.emptyList();

    }
}
