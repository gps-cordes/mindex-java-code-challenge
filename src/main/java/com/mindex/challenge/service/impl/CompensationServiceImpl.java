package com.mindex.challenge.service.impl;

import com.mindex.challenge.data.Compensation;

import java.util.Optional;

public class CompensationServiceImpl {

    public Compensation createCompensation(String employeeId){
        return new Compensation();
    }

    public Optional<Compensation> readCompensation(String employeeId){
        return Optional.empty();
    }
}
