package com.b1a9idps.client.confing;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.b1a9idps.client.service.handler.RestTemplateResponseErrorHandler;

@Configuration(proxyBeanMethods = false)
public class RestTemplateConfig {

    @Bean
    public RestTemplateBuilder defaultHandlingRestTemplateBuilder() {
        return new RestTemplateBuilder();
    }

    @Bean
    public RestTemplateBuilder customHandlingRestTemplateBuilder() {
        return new RestTemplateBuilder()
                .errorHandler(new RestTemplateResponseErrorHandler());
    }
}
