package com.mindex.challenge.config;

import com.mindex.challenge.error.ErrorResponseFactory;
import com.mindex.challenge.error.exception.CompensationAlreadyExistsException;
import com.mindex.challenge.error.exception.EmployeeDoesNotExistException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
public class CustomExceptionHandler {

    @Autowired
    private ErrorResponseFactory errorResponseFactory;
    @ExceptionHandler(EmployeeDoesNotExistException.class)
    public ResponseEntity<Object> handleDataNotFoundException(EmployeeDoesNotExistException ex, HttpServletRequest request) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        return new ResponseEntity<>(errorResponseFactory.createErrorResponse(ex, request, status, "Employee Id Invalid"),status);
    }

    @ExceptionHandler(CompensationAlreadyExistsException.class)
    public ResponseEntity<Object> handleDataNotFoundException(CompensationAlreadyExistsException ex, HttpServletRequest request) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        return new ResponseEntity<>(errorResponseFactory.createErrorResponse(ex, request, status, "Compensation already exists"), HttpStatus.BAD_REQUEST);
    }
}