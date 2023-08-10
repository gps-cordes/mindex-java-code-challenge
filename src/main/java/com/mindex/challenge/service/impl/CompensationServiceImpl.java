package com.mindex.challenge.service.impl;

import com.mindex.challenge.data.Compensation;
import com.mindex.challenge.service.CompensationService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CompensationServiceImpl implements CompensationService {

    @Override
    public Compensation createCompensation(String employeeId, Compensation compensation){
        return new Compensation();
    }

    @Override
    public Optional<Compensation> readCompensation(String employeeId){
        return Optional.empty();
    }
}
