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
import com.b1a9idps.client.externals.service.ServerService;
import com.b1a9idps.client.externals.response.ServerResponse;

@RestController
@RequestMapping("/client")
public class ClientController {

    private final ServerService serverService;

    public ClientController(ServerService serverService) {
        this.serverService = serverService;
    }

    @GetMapping("/default")
    public ResponseEntity<ClientResponse> defaultHandler(@RequestParam(value = "status_code", required = false) Optional<Integer> statusCode) throws ServerRestTemplateException {
        ResponseEntity<ServerResponse> serverResponse = serverService.defaultHandlerGet(statusCode);
        return ResponseEntity.status(serverResponse.getStatusCode())
                .body(ClientResponse.newInstance(serverResponse.getBody()));
    }

    @GetMapping("/custom")
    public ResponseEntity<ClientResponse> customHandler(@RequestParam(value = "status_code", required = false) Optional<Integer> statusCode) throws ServerRestTemplateException {
        ResponseEntity<ServerResponse> serverResponse = serverService.customHandlerGet(statusCode);

        if (serverResponse.getStatusCode().isError()) {
            // 4xx, 5xxの場合、ResponseEntityのレスポンスボディは、new ServerResponse(null, null)となる
            throw new ServerRestTemplateException(new ErrorResponse("custom handler error"), serverResponse.getStatusCode());
        }

        return ResponseEntity.status(serverResponse.getStatusCode())
                .body(ClientResponse.newInstance(serverResponse.getBody()));
    }

    @ExceptionHandler(ServerRestTemplateException.class)
    public ResponseEntity<ErrorResponse> handleServerRestTemplateException(ServerRestTemplateException e) {
        return ResponseEntity.status(e.getStatus())
                .body(e.getResponse());
    }

}
