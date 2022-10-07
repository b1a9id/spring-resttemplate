package com.b1a9idps.client.service;

import java.util.Optional;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.b1a9idps.client.service.response.ServerResponse;

@Service
public class ServerService {
    private static final String BASE_URI = "http://localhost:8081/api";
    private static final String BASE_PATH = "/server";

    private final RestTemplate defaultHandlingRestTemplate;
    private final RestTemplate customHandlingRestTemplate;

    public ServerService(
            RestTemplateBuilder defaultHandlingRestTemplateBuilder,
            RestTemplateBuilder customHandlingRestTemplateBuilder) {
        this.defaultHandlingRestTemplate = defaultHandlingRestTemplateBuilder.rootUri(BASE_URI).build();
        this.customHandlingRestTemplate = customHandlingRestTemplateBuilder.rootUri(BASE_URI).build();
    }

    public ResponseEntity<ServerResponse> defaultHandlingGet(Optional<Integer> statusCode) {
        var builder = UriComponentsBuilder.fromPath(BASE_PATH);
        if (statusCode.isPresent()) {
            builder.queryParam("status_code", statusCode.get());
        }
        var uri = builder.toUriString();

        var requestEntity = RequestEntity.get(uri).build();

        return defaultHandlingRestTemplate.exchange(requestEntity, ServerResponse.class);
    }
}
