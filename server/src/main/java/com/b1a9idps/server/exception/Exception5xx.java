package com.b1a9idps.server.exception;

import org.springframework.http.HttpStatus;

public class Exception5xx extends CustomException {
    public Exception5xx(String message, HttpStatus status) {
        super(message, status);
    }
}
