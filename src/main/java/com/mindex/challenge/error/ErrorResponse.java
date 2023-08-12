package com.mindex.challenge.error;

import org.springframework.http.HttpStatus;

import java.time.ZonedDateTime;

public class ErrorResponse {
    private String error;
    private String message;
    private String path;
    private HttpStatus status;
    private ZonedDateTime timestamp;

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }

    public void setStatus(HttpStatus status) {
        this.status = status;
    }

    public int getStatus() {
        return status.value();
    }

    public void setTimestamp(ZonedDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public ZonedDateTime getTimestamp() {
        return timestamp;
    }
}
