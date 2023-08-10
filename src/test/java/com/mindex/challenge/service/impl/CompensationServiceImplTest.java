package com.mindex.challenge.service.impl;

import com.mindex.challenge.dao.CompensationRepository;
import com.mindex.challenge.data.Compensation;
import com.mindex.challenge.data.Employee;
import com.mindex.challenge.exception.CompensationAlreadyExistsException;
import com.mindex.challenge.exception.EmployeeDoesNotExistException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CompensationServiceImplTest{



    @Autowired
    private CompensationServiceImpl compensationService;

    @Autowired
    private CompensationRepository compensationRepository;

    @Autowired
    private EmployeeServiceImpl employeeService;

    @Test(expected = EmployeeDoesNotExistException.class)
    public void createCompensation_employeeDoesNotExist() throws Exception{
        // given employee id is provided
        String id = "ThisIdDoesNotExist";
        Compensation compensation = new Compensation();
        // and the employee does not exist
        compensationService.createCompensation(id, compensation);
        // then throw an error
    }

    @Test(expected = EmployeeDoesNotExistException.class)
    public void createCompensation_nullInput() {
        // given a NULL employee id is provided
        Compensation compensation = new Compensation();
        // then throw an error
        compensationService.createCompensation(null,compensation );

    }

    @Test
    public void createCompensation_employeeDoesExist_compensationDoesNotExist() {
        // given an employeeId is provided
        // and the employee exists
        Employee testEmployee = createNewTestEmployee();
        Compensation testCompensation = new Compensation();
        testCompensation.setEffectiveDate(LocalDate.now());
        testCompensation.setSalary(100000);
        // and the compensation does not exist
        assertNull(compensationRepository.findByEmployeeEmployeeId(testEmployee.getEmployeeId()));

        // then create the compensation
        Compensation compensationResponse = compensationService.createCompensation(testEmployee.getEmployeeId(), testCompensation);
        // and return the compensation
        testCompensation.setEmployee(testEmployee);
        assertCompensationEquivalence(testCompensation, compensationResponse);

        // and the compensation was stored the required fields and the employeeId filled in
        Employee expectedDBEmployee = new Employee();
        expectedDBEmployee.setEmployeeId(testEmployee.getEmployeeId());
        testCompensation.setEmployee(expectedDBEmployee);
        assertCompensationEquivalence(testCompensation, compensationRepository.findByEmployeeEmployeeId(testEmployee.getEmployeeId()));
    }

    @Test(expected = CompensationAlreadyExistsException.class)
    public void createCompensation_employeeDoesExist_compensationAlreadyExists(){
        // given an employeeId is provided
        // and the employee exists
        Employee testEmployee = createNewTestEmployee();
        Compensation testCompensation = new Compensation();
        testCompensation.setEffectiveDate(LocalDate.now());
        testCompensation.setSalary(100000);

        Employee DBCompensationEmployee = new Employee();
        DBCompensationEmployee.setEmployeeId(testEmployee.getEmployeeId());
        testCompensation.setEmployee(DBCompensationEmployee);
        // and the compensation already exists
        compensationRepository.insert(testCompensation);

        // then try to create a new compensation
        testCompensation.setSalary(120000);
        testCompensation.setEffectiveDate(LocalDate.now());
        compensationService.createCompensation(testEmployee.getEmployeeId(), testCompensation);
        // then return an error
        //
        // NOTE: UPDATE is not specified and multiple compensations are not described.
        //          the effective date implies that there should be historical compensations supported,
        //          but I am choosing to support basic functionality until further requirements are received
    }

    @Test
    public void readCompensation_nullInput(){
        // given an employeeId is null
        // throw an error could alert the caller of the API what is wrong
    }

    // Read
    @Test
    public void readCompensation_employeeDoesNotExist(){
        // given an employeeId is provided
        // and the employee does not exist
        //  Throw an error can alert the caller of the API what is wrong
    }

    @Test
    public void readCompensation_employeeDoesExist_compensationDoesNotExist(){
        // given an employeeId is provided
        // and the employee does exist
        // and the compensation does not exist
        // then return an empty response
    }

    @Test
    public void readCompensation_employeeDoesExist_compensationExists(){
        // given an employeeId is provided
        // and the employee does exist
        // and the compensation does  exist
        // then return compensation
    }

    private Employee createNewTestEmployee(){
        Employee testEmployee = new Employee();
        testEmployee.setFirstName("first");
        testEmployee.setLastName("last");
        testEmployee.setPosition("Developer");
        testEmployee.setDepartment("Realty");
        return employeeService.create(testEmployee);
    }

    private static void assertCompensationEquivalence(Compensation expected, Compensation actual){
        // check employee
        assertEquals(expected.getEmployee().getEmployeeId(), actual.getEmployee().getEmployeeId());
        // this could go into a utility method shared between the tests
        assertEquals(expected.getEmployee().getFirstName(), actual.getEmployee().getFirstName());
        assertEquals(expected.getEmployee().getLastName(), actual.getEmployee().getLastName());
        assertEquals(expected.getEmployee().getDepartment(), actual.getEmployee().getDepartment());
        assertEquals(expected.getEmployee().getPosition(), actual.getEmployee().getPosition());
        // check compensation
        assertEquals(expected.getEffectiveDate(), actual.getEffectiveDate());
        assertEquals(expected.getSalary(), actual.getSalary());

    }

}
