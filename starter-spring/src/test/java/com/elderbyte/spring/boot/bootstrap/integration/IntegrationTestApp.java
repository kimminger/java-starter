package com.elderbyte.spring.boot.bootstrap.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import java.net.UnknownHostException;

import static com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES;
import static com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES;


@SpringBootApplication
@Configuration
public class IntegrationTestApp {

    private static final Logger log = LoggerFactory.getLogger(IntegrationTestApp.class);

    @Bean
    public ObjectMapper objectMapper(){
        var mapper = new ObjectMapper();

        var config = mapper.getDeserializationConfig()
                .without(
                        FAIL_ON_UNKNOWN_PROPERTIES,
                        FAIL_ON_IGNORED_PROPERTIES
                );

        mapper.setConfig(config);

        return mapper;
    }

    public static void main(String[] args) throws UnknownHostException {

        SpringApplication app = new SpringApplication(IntegrationTestApp.class);
        Environment env = app.run(args).getEnvironment();

    }


}
