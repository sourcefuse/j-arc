package com.sourcefuse.jarc.services.usertenantservice.exceptions;

import org.springframework.http.HttpStatus;


public class CommonRuntimeException extends RuntimeException {

    private final HttpStatus status;
    private final String message;

    public CommonRuntimeException(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }

    public CommonRuntimeException(String message, HttpStatus status, String message1) {
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