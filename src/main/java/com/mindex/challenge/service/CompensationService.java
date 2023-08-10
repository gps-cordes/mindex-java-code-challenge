package com.mindex.challenge.service;

import com.mindex.challenge.data.Compensation;

import java.util.Optional;

public interface CompensationService {
    Compensation createCompensation(String employeeId, Compensation compensation);

    Optional<Compensation> readCompensation(String employeeId);
}
