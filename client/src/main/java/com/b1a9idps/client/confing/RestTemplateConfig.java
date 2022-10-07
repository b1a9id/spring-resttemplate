package com.b1a9idps.client.confing;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.b1a9idps.client.externals.handler.RestTemplateResponseErrorHandler;

@Configuration(proxyBeanMethods = false)
public class RestTemplateConfig {

    @Bean
    public RestTemplateBuilder defaultHandlerRestTemplateBuilder() {
        return new RestTemplateBuilder();
    }

    @Bean
    public RestTemplateBuilder customHandlerRestTemplateBuilder() {
        return new RestTemplateBuilder()
                .errorHandler(new RestTemplateResponseErrorHandler());
    }
}
