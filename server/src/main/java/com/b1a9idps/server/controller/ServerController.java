package com.b1a9idps.server.controller;

import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.b1a9idps.server.exception.CustomException;
import com.b1a9idps.server.response.ErrorResponse;
import com.b1a9idps.server.response.ServerResponse;

@RestController
@RequestMapping("/server")
public class ServerController {

    @GetMapping
    public ServerResponse index(@RequestParam(value = "status_code", required = false) Optional<Integer> statusCode) {
        HttpStatus status = statusCode
                .map(HttpStatus::resolve)
                .orElse(null);
        if (status != null && status.isError()) {
            throw new CustomException(status.getReasonPhrase(), status);
        } else {
            return new ServerResponse(1, "name");
        }
    }

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ErrorResponse> handle(CustomException e) {
        return ResponseEntity
                .status(e.getStatus())
                .body(new ErrorResponse(e.getMessage()));
    }
}
