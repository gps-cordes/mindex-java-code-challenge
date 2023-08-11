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
//      TODO: implement NO_CONTENT response
//        assertEquals(HttpStatus.NO_CONTENT, noCompensationResponse.getStatusCode());
        assertEquals(null, noCompensationResponse.getBody());
        // Create a new compensation
        Compensation compensationRequest = new Compensation();
        compensationRequest.setSalary(10000);
        compensationRequest.setEffectiveDate(LocalDate.now());
        assertEquals(HttpStatus.OK, restTemplate.postForEntity(employeeIdCompensationUrl, compensationRequest, Compensation.class, johnLennonEmployeeId).getStatusCode());

        // read the new compensation

        Compensation compensationResponse = restTemplate.getForEntity(employeeIdCompensationUrl, Compensation.class, johnLennonEmployeeId).getBody();
        Compensation expectedCompensation = new Compensation();
        expectedCompensation.setEmployee(employeeService.read(johnLennonEmployeeId)); // will be fully hydrated
        expectedCompensation.setEffectiveDate(compensationRequest.getEffectiveDate());
        expectedCompensation.setSalary(compensationRequest.getSalary());

        assertCompensationEquivalence(expectedCompensation, compensationResponse);

        // try to create a new compensation, expect an error
        ResponseEntity<Compensation> response = restTemplate.postForEntity(employeeIdCompensationUrl, compensationRequest, Compensation.class, johnLennonEmployeeId);
        assertNotEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());

    }

    @Test
    public void createCompensation_employeeDoesNotExist(){
        String johnLennonEmployeeId = "thisEmployeeDoesNotExist";

        // given employee does not have compensation
        ResponseEntity<Compensation> response = restTemplate.postForEntity(employeeIdCompensationUrl, new Compensation(), Compensation.class, johnLennonEmployeeId);
        assertNotEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        // Create a new compensation
    }

    @Test
    public void readCompensation_employeeDoesNotExist(){
        String johnLennonEmployeeId = "thisEmployeeDoesNotExist";

        // given employee does not have compensation
        ResponseEntity<Compensation> response = restTemplate.getForEntity(employeeIdCompensationUrl, Compensation.class, johnLennonEmployeeId);
        assertNotEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        // Create a new compensation
    }
}
