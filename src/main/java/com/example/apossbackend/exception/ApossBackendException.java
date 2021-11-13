package com.example.apossbackend.exception;

import org.springframework.http.HttpStatus;

public class ApossBackendException extends RuntimeException{

    private HttpStatus status;
    private String message;

    public ApossBackendException(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }

    public ApossBackendException(String message, HttpStatus status, String message1) {
        super(message);
        this.status = status;
        this.message = message1;
    }

    public HttpStatus getStatus() {
        return status;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
