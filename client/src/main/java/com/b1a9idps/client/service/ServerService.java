package com.b1a9idps.client.service;

import java.io.IOException;
import java.util.Optional;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.b1a9idps.client.exception.ServerRestTemplateException;
import com.b1a9idps.client.response.ErrorResponse;
import com.b1a9idps.client.service.response.ServerResponse;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class ServerService {
    private static final String BASE_URI = "http://localhost:8081/api";
    private static final String BASE_PATH = "/server";

    private final RestTemplate defaultHandlingRestTemplate;
    private final RestTemplate customHandlingRestTemplate;
    private final ObjectMapper objectMapper;

    public ServerService(
            RestTemplateBuilder defaultHandlingRestTemplateBuilder,
            RestTemplateBuilder customHandlingRestTemplateBuilder,
            ObjectMapper objectMapper) {
        this.defaultHandlingRestTemplate = defaultHandlingRestTemplateBuilder.rootUri(BASE_URI).build();
        this.customHandlingRestTemplate = customHandlingRestTemplateBuilder.rootUri(BASE_URI).build();
        this.objectMapper = objectMapper;
    }

    public ResponseEntity<ServerResponse> defaultHandlingGet(Optional<Integer> statusCode) throws ServerRestTemplateException {
        var builder = UriComponentsBuilder.fromPath(BASE_PATH);
        if (statusCode.isPresent()) {
            builder.queryParam("status_code", statusCode.get());
        }
        var uri = builder.toUriString();

        var requestEntity = RequestEntity.get(uri).build();

        try {
            return defaultHandlingRestTemplate.exchange(requestEntity, ServerResponse.class);
        } catch (RestClientException e) {
            if (e instanceof HttpStatusCodeException exception) {
                try {
                    var errorResponse = objectMapper.readValue(exception.getResponseBodyAsString(), ErrorResponse.class);
                    throw new ServerRestTemplateException(errorResponse, exception.getStatusCode());
                } catch (IOException ioException) {
                    throw new ServerRestTemplateException("invalid response");
                }
            }
            throw new ServerRestTemplateException("error");
        }
    }
}
