package com.b1a9idps.client.externals.service;

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
import com.b1a9idps.client.externals.response.ServerResponse;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class ServerService {
    private static final String BASE_URI = "http://localhost:8081/api";
    private static final String BASE_PATH = "/server";

    private final RestTemplate defaultHandlerRestTemplate;
    private final RestTemplate customHandlerRestTemplate;
    private final ObjectMapper objectMapper;

    public ServerService(
            RestTemplateBuilder defaultHandlerRestTemplateBuilder,
            RestTemplateBuilder customHandlerRestTemplateBuilder,
            ObjectMapper objectMapper) {
        this.defaultHandlerRestTemplate = defaultHandlerRestTemplateBuilder.rootUri(BASE_URI).build();
        this.customHandlerRestTemplate = customHandlerRestTemplateBuilder.rootUri(BASE_URI).build();
        this.objectMapper = objectMapper;
    }

    /**
     * RestTemplateを使った通信のエラーハンドリングに {@link org.springframework.web.client.DefaultResponseErrorHandler} を利用
     */
    public ResponseEntity<ServerResponse> defaultHandlerGet(Optional<Integer> statusCode) throws ServerRestTemplateException {
        var requestEntity = buildRequestEntity(statusCode);

        try {
            return defaultHandlerRestTemplate.exchange(requestEntity, ServerResponse.class);
        } catch (HttpStatusCodeException e) {
            try {
                var errorResponse = objectMapper.readValue(e.getResponseBodyAsString(), ErrorResponse.class);
                throw new ServerRestTemplateException(errorResponse, e.getStatusCode());
            } catch (IOException ioException) {
                throw new ServerRestTemplateException("invalid response");
            }
        } catch (RestClientException e) {
            throw new ServerRestTemplateException("error");
        }
    }

    /**
     * RestTemplateを使った通信のエラーハンドリングに {@link com.b1a9idps.client.externals.handler.RestTemplateResponseErrorHandler} を利用
     */
    public ResponseEntity<ServerResponse> customHandlerGet(Optional<Integer> statusCode) {
        var requestEntity = buildRequestEntity(statusCode);

        return customHandlerRestTemplate.exchange(requestEntity, ServerResponse.class);
    }

    private RequestEntity<Void> buildRequestEntity(Optional<Integer> statusCode) {
        var builder = UriComponentsBuilder.fromPath(BASE_PATH);
        if (statusCode.isPresent()) {
            builder.queryParam("status_code", statusCode.get());
        }
        var uri = builder.toUriString();

        return RequestEntity.get(uri).build();
    }
}
