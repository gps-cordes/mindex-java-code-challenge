package com.mindex.challenge.config;

import com.mindex.challenge.error.ErrorResponse;
import com.mindex.challenge.error.exception.CompensationAlreadyExistsException;
import com.mindex.challenge.error.exception.EmployeeDoesNotExistException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class CustomExceptionHandler

{

    @ExceptionHandler(EmployeeDoesNotExistException.class)
    public ResponseEntity<Object> handleDataNotFoundException(EmployeeDoesNotExistException ex) {
        String errorMessage = "Employee ID Invalid";
        String details = ex.getMessage();
        ErrorResponse errorResponse = new ErrorResponse(errorMessage, details);
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(CompensationAlreadyExistsException.class)
    public ResponseEntity<Object> handleDataNotFoundException(CompensationAlreadyExistsException ex) {
        String errorMessage = "Compensation already exists";
        String details = ex.getMessage();
        ErrorResponse errorResponse = new ErrorResponse(errorMessage, details);
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
}