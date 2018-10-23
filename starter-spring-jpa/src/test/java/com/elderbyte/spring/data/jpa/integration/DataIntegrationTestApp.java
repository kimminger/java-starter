package com.elderbyte.spring.data.jpa.integration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.Environment;

import java.net.UnknownHostException;


@SpringBootApplication
public class DataIntegrationTestApp {

    private static final Logger log = LoggerFactory.getLogger(DataIntegrationTestApp.class);

    public static void main(String[] args) throws UnknownHostException {

        SpringApplication app = new SpringApplication(DataIntegrationTestApp.class);
        Environment env = app.run(args).getEnvironment();

    }

}