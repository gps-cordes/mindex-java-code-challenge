package com.mindex.challenge.service.impl;

import com.mindex.challenge.data.Employee;
import com.mindex.challenge.data.ReportingStructure;
import com.mindex.challenge.service.EmployeeService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class EmployeeServiceImplTest {

    private String employeeUrl;
    private String employeeIdUrl;
    private String employeeReportIdUrl;

    @Autowired
    private EmployeeService employeeService;

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Before
    public void setup() {
        employeeUrl = "http://localhost:" + port + "/employee";
        employeeIdUrl = "http://localhost:" + port + "/employee/{id}";
        employeeReportIdUrl = "http://localhost:" + port + "/employee/{id}/report";
    }

    @Test
    public void testCreateReadUpdate() {
        Employee testEmployee = new Employee();
        testEmployee.setFirstName("John");
        testEmployee.setLastName("Doe");
        testEmployee.setDepartment("Engineering");
        testEmployee.setPosition("Developer");

        // Create checks
        Employee createdEmployee = restTemplate.postForEntity(employeeUrl, testEmployee, Employee.class).getBody();

        assertNotNull(createdEmployee.getEmployeeId());
        assertEmployeeEquivalence(testEmployee, createdEmployee);


        // Read checks
        Employee readEmployee = restTemplate.getForEntity(employeeIdUrl, Employee.class, createdEmployee.getEmployeeId()).getBody();
        assertEquals(createdEmployee.getEmployeeId(), readEmployee.getEmployeeId());
        assertEmployeeEquivalence(createdEmployee, readEmployee);


        // Update checks
        readEmployee.setPosition("Development Manager");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Employee updatedEmployee =
                restTemplate.exchange(employeeIdUrl,
                        HttpMethod.PUT,
                        new HttpEntity<Employee>(readEmployee, headers),
                        Employee.class,
                        readEmployee.getEmployeeId()).getBody();

        assertEmployeeEquivalence(readEmployee, updatedEmployee);
    }

    // Test 0,1 and more than 0
    @Test
    public void testEmployeeReport_JohnLennon_4() {
        String johnLennonEmployeeId = "16a596ae-edd3-4847-99fe-c4518e82c86f";
        // assert that test data Exists
        Employee johnLennon = restTemplate.getForEntity(employeeIdUrl, Employee.class, johnLennonEmployeeId).getBody();
        assertNotNull("John Lennon does not exist in database", johnLennon);
        assertEquals("John Lennon Id does not match",johnLennon.getEmployeeId(), johnLennonEmployeeId);

        // when GET /employee/{id}/report is requested

        // it should return 4
        ReportingStructure johnLennonReport = restTemplate.getForEntity(employeeReportIdUrl, ReportingStructure.class, johnLennonEmployeeId).getBody();
        assertEquals("John Lennon direct reports count is wrong", 4, johnLennonReport.getNumberOfReports());
        assertEmployeeEquivalence(johnLennonReport.getEmployee(), johnLennon);

    }

    @Test
    public void testEmployeeReport_paulMcCartney_0() {
        String testEmployeeId = "b7839309-3348-463b-a7e3-5de1c168beb3";
        // assert that test data Exists
        Employee employee = restTemplate.getForEntity(employeeIdUrl, Employee.class, testEmployeeId).getBody();
        assertNotNull("employee does not exist in database", employee);
        assertEquals("employeeId does not match",employee.getEmployeeId(), testEmployeeId);

        // when GET /employee/{id}/report is requested

        // it should return 0
        ReportingStructure employeeReport = restTemplate.getForEntity(employeeReportIdUrl, ReportingStructure.class, testEmployeeId).getBody();
        assertEquals("Employee direct reports count is wrong", 0, employeeReport.getNumberOfReports());
        assertEmployeeEquivalence(employeeReport.getEmployee(), employee);
    }

    @Test
    public void testEmployeeReport_1() {

        Employee testEmployee = new Employee();
        testEmployee.setFirstName("John");
        testEmployee.setLastName("Doe");
        testEmployee.setDepartment("Engineering");
        testEmployee.setPosition("Developer");

        // Create leaf employee
        Employee leafEmployee = restTemplate.postForEntity(employeeUrl, testEmployee, Employee.class).getBody();

        // Create root employee
        Employee testEmployee2 = new Employee();
        testEmployee2.setFirstName("Jane");
        testEmployee2.setLastName("Doe");
        testEmployee2.setDepartment("Engineering");
        testEmployee2.setPosition("Developer 2");
        testEmployee2.setDirectReports(List.of(leafEmployee));
        Employee rootEmployee = restTemplate.postForEntity(employeeUrl, testEmployee2, Employee.class ).getBody();

        // skip data assertion since previous test covers insert functionality

        // when GET /employee/{id}/report is requested

        // it should return 0
        ReportingStructure employeeReport = restTemplate.getForEntity(employeeReportIdUrl, ReportingStructure.class, rootEmployee.getEmployeeId()).getBody();
        assertEquals("Employee direct reports count is wrong", 1, employeeReport.getNumberOfReports());
        assertEmployeeEquivalence(employeeReport.getEmployee(), rootEmployee);
    }

    @Test
    public void testEmployeeReportDNE(){
        String testEmployeeId = "ThisEmployeeIdDoesNotExist";
        // verify that the employee ID does not actually exists
        assertEquals("employee should not exist in the database", HttpStatus.INTERNAL_SERVER_ERROR, restTemplate.getForEntity(employeeIdUrl, Employee.class, testEmployeeId).getStatusCode());

        // return a 204 (not 404, which implies the rest api path does not exist at all)
        ResponseEntity response = restTemplate.getForEntity(employeeReportIdUrl, ReportingStructure.class, "ThisEmployeeIdDoesNotExist");
        assertEquals("employee report for an employee that should not exist does not return no content status code", HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    private static void assertEmployeeEquivalence(Employee expected, Employee actual) {
        assertEquals(expected.getFirstName(), actual.getFirstName());
        assertEquals(expected.getLastName(), actual.getLastName());
        assertEquals(expected.getDepartment(), actual.getDepartment());
        assertEquals(expected.getPosition(), actual.getPosition());
    }
}
