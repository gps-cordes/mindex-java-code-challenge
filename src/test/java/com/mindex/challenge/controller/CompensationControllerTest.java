package com.mindex.challenge.controller;

import com.mindex.challenge.data.Compensation;
import com.mindex.challenge.service.EmployeeService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;

import static com.mindex.challenge.TestUtil.assertCompensationEquivalence;
import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CompensationControllerTest {

    private String employeeIdCompensationUrl;

    @Autowired
    private EmployeeService employeeService;

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Before
    public void setup() {
        employeeIdCompensationUrl = "http://localhost:" + port + "/employee/{id}/compensation";
    }

    @Test
    public void createCompensation_employeeExists_createReadCompensation(){
        String johnLennonEmployeeId = "16a596ae-edd3-4847-99fe-c4518e82c86f";

        // given employee does not have compensation
        ResponseEntity<Compensation> noCompensationResponse = restTemplate.getForEntity(employeeIdCompensationUrl, Compensation.class, johnLennonEmployeeId);
        assertEquals(HttpStatus.NO_CONTENT, noCompensationResponse.getStatusCode());
        assertNull(noCompensationResponse.getBody());
        // Create a new compensation
        Compensation compensationRequest = new Compensation();
        compensationRequest.setSalary(10000);
        compensationRequest.setEffectiveDate(LocalDate.now().minusDays(30));
        assertEquals(HttpStatus.OK, restTemplate.postForEntity(employeeIdCompensationUrl, compensationRequest, Compensation.class, johnLennonEmployeeId).getStatusCode());

        // read the new compensation

        Compensation compensationResponse = restTemplate.getForEntity(employeeIdCompensationUrl, Compensation.class, johnLennonEmployeeId).getBody();
        Compensation expectedCompensation = new Compensation();
        expectedCompensation.setEmployee(employeeService.read(johnLennonEmployeeId)); // will be fully hydrated
        expectedCompensation.setEffectiveDate(compensationRequest.getEffectiveDate());
        expectedCompensation.setSalary(compensationRequest.getSalary());

        assertCompensationEquivalence(expectedCompensation, compensationResponse);

        // try to create a new compensation, expect an error because collision with existing date
        ResponseEntity<Compensation> response = restTemplate.postForEntity(employeeIdCompensationUrl, compensationRequest, Compensation.class, johnLennonEmployeeId);
        assertNotEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        // can insert a new record because different date
        compensationRequest.setSalary(12000);
        compensationRequest.setEffectiveDate(LocalDate.now().minusDays(2));
        response = restTemplate.postForEntity(employeeIdCompensationUrl, compensationRequest, Compensation.class, johnLennonEmployeeId);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        // expect this to be the latest salary from the read
        compensationResponse = restTemplate.getForEntity(employeeIdCompensationUrl, Compensation.class, johnLennonEmployeeId).getBody();
        expectedCompensation.setEmployee(employeeService.read(johnLennonEmployeeId)); // will be fully hydrated
        expectedCompensation.setEffectiveDate(compensationRequest.getEffectiveDate());
        expectedCompensation.setSalary(compensationRequest.getSalary());

        assertCompensationEquivalence(expectedCompensation, compensationResponse);

    }

    @Test
    public void createCompensation_employeeDoesNotExist(){
        String johnLennonEmployeeId = "thisEmployeeDoesNotExist";

        // given employee does not have compensation
        ResponseEntity<Compensation> response = restTemplate.postForEntity(employeeIdCompensationUrl, new Compensation(), Compensation.class, johnLennonEmployeeId);
        assertNotEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        // Create a new compensation
    }

    @Test
    public void readCompensation_employeeDoesNotExist(){
        String johnLennonEmployeeId = "thisEmployeeDoesNotExist";

        // given employee does not have compensation
        ResponseEntity<Compensation> response = restTemplate.getForEntity(employeeIdCompensationUrl, Compensation.class, johnLennonEmployeeId);
        assertNotEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        // Create a new compensation
    }
}
