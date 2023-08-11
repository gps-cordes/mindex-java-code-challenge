package com.mindex.challenge.exception;

public class EmployeeDoesNotExistException extends RuntimeException{

    public EmployeeDoesNotExistException(String message){
        super(message);
    }


    public EmployeeDoesNotExistException(){
        super();
    }
}
