package com.b1a9idps.server.exception;

import org.springframework.http.HttpStatus;

public class Exception4xx extends CustomException {

    public Exception4xx(String message, HttpStatus status) {
        super(message, status);
    }
}
