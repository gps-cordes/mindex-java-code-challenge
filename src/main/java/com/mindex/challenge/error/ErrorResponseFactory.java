package com.mindex.challenge.error;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.time.ZonedDateTime;

@Service
public class ErrorResponseFactory {
    public ErrorResponse createErrorResponse(Exception ex, HttpServletRequest request, HttpStatus httpStatus, String errorMessage){
        String details = ex.getMessage();
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setError(errorMessage);
        errorResponse.setMessage(details);
        errorResponse.setPath(request.getRequestURI());
        errorResponse.setStatus(httpStatus);
        errorResponse.setTimestamp(ZonedDateTime.now());
        return errorResponse;
    }
}
