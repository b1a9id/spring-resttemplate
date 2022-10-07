package com.b1a9idps.client.exception;

import org.springframework.http.HttpStatus;

import com.b1a9idps.client.response.ErrorResponse;

public class ServerRestTemplateException extends Exception {
    private final ErrorResponse response;
    private final HttpStatus status;

    public ServerRestTemplateException(String message) {
        super(message);
        this.response = null;
        this.status = HttpStatus.INTERNAL_SERVER_ERROR;
    }

    public ServerRestTemplateException(ErrorResponse response, HttpStatus status) {
        this.response = response;
        this.status = status;
    }

    public ErrorResponse getResponse() {
        return response;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
