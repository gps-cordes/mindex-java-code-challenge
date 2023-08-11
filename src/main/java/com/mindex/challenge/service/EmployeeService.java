package com.mindex.challenge.service;

import com.mindex.challenge.data.Employee;
import com.mindex.challenge.response.ReportingStructure;

public interface EmployeeService {
    Employee create(Employee employee);
    Employee read(String id);
    Employee update(Employee employee);

    ReportingStructure report(String id);
}
