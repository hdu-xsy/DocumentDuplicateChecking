package com.xsy.documentduplicatechecking.utils;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;

public class RestTemplateUtils {

    private static RestTemplate restTemplate;

    public synchronized static RestTemplate getInstance() {
        if (restTemplate == null) {
            StringHttpMessageConverter m = new StringHttpMessageConverter(StandardCharsets.UTF_8);
            restTemplate = new RestTemplateBuilder().additionalMessageConverters(m).build();
        }
        return restTemplate;
    }

}