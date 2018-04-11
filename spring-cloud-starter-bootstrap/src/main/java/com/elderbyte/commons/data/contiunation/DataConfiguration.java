package com.elderbyte.commons.data.contiunation;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataConfiguration {

    @Bean
    public JsonContinuationTokenBuilder JsonContinuationTokenBuilder(ObjectMapper mapper){
        return new JsonContinuationTokenBuilder(mapper);
    }
}
