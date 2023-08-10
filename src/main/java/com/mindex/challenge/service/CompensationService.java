package com.mindex.challenge.service;

import com.mindex.challenge.data.Compensation;
import com.mindex.challenge.exception.EmployeeDoesNotExistException;

import java.util.Optional;

public interface CompensationService {
    Compensation createCompensation(String employeeId, Compensation compensation) throws EmployeeDoesNotExistException;

    Optional<Compensation> readCompensation(String employeeId);
}
