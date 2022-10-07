package com.b1a9idps.server.controller;

import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.b1a9idps.server.exception.Exception4xx;
import com.b1a9idps.server.exception.Exception5xx;
import com.b1a9idps.server.response.ErrorResponse;
import com.b1a9idps.server.response.ServerResponse;

@RestController
@RequestMapping("/server")
public class ServerController {

    @GetMapping
    public ServerResponse index(@RequestParam(value = "status_code", required = false) Optional<Integer> statusCode) {
        if (statusCode.isEmpty()) {
            return new ServerResponse(1, "name");
        }

        HttpStatus status = statusCode
                .map(HttpStatus::resolve)
                .orElseThrow();
        if (status.is4xxClientError()) {
            throw new Exception4xx(status.getReasonPhrase(), status);
        } else if (status.is5xxServerError()) {
            throw new Exception5xx(status.getReasonPhrase(), status);
        } else {
            throw new IllegalArgumentException();
        }
    }

    @ExceptionHandler({Exception4xx.class, Exception5xx.class})
    public ResponseEntity<ErrorResponse> handle(Exception4xx e) {
        return ResponseEntity
                .status(e.getStatus())
                .body(new ErrorResponse(e.getMessage()));
    }
}
