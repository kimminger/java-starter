package com.elderbyte.spring.cloud.bootstrap.data;

import com.elderbyte.commons.data.contiunation.JsonContinuationToken;
import com.elderbyte.commons.data.contiunation.JsonContinuationTokenBuilder;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataConfiguration {

    @Bean
    public JsonContinuationTokenBuilder JsonContinuationTokenBuilder(ObjectMapper mapper){
        var builder = new JsonContinuationTokenBuilder(mapper);
        JsonContinuationToken.setBuilder(builder);
        return builder;
    }
}
