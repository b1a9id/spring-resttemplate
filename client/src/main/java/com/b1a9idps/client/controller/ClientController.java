package com.b1a9idps.client.controller;

import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.b1a9idps.client.exception.ServerRestTemplateException;
import com.b1a9idps.client.response.ClientResponse;
import com.b1a9idps.client.response.ErrorResponse;
import com.b1a9idps.client.service.ServerService;
import com.b1a9idps.client.service.response.ServerResponse;

@RestController
@RequestMapping("/client")
public class ClientController {

    private final ServerService serverService;

    public ClientController(ServerService serverService) {
        this.serverService = serverService;
    }

    @GetMapping("/default")
    public ResponseEntity<ClientResponse> index(@RequestParam(value = "status_code", required = false) Optional<Integer> statusCode) throws ServerRestTemplateException {
        ResponseEntity<ServerResponse> serverResponse = serverService.defaultHandlingGet(statusCode);
        var builder = ResponseEntity.status(serverResponse.getStatusCode());

        return ResponseEntity.status(serverResponse.getStatusCode())
                .body(ClientResponse.newInstance(serverResponse.getBody()));
    }

    @ExceptionHandler(ServerRestTemplateException.class)
    public ResponseEntity<ErrorResponse> handleServerRestTemplateException(ServerRestTemplateException e) {
        return ResponseEntity.status(e.getStatus())
                .body(e.getResponse());
    }

}