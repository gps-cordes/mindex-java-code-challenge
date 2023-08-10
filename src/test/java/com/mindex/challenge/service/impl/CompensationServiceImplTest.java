package com.mindex.challenge.service.impl;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
public class CompensationServiceImplTest {


    @Test
    public void createCompensation_employeeDoesNotExist() {
        // given employee id is provided
        // and the employee does not exist
        // then throw an error
    }

    @Test
    public void createCompensation_nullInput() {
        // given a NULL employee id is provided
        // then throw an error? Does mongoDB cover this?

    }

    @Test
    public void createCompensation_employeeDoesExist_compensationDoesNotExist() {
        // given an employeeId is provided
        // and the employee exists
        // and the compensation does not exist
        // then create the compensation
        // and return the compensation

    }

    @Test
    public void createCompensation_employeeDoesExist_compensationAlreadyExists(){
        // given an employeeId is provided
        // and the employee exists
        // and the compensation does exist
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

}
