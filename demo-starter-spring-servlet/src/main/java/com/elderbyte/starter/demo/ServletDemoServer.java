package com.elderbyte.starter.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.Environment;

import java.net.InetAddress;
import java.net.UnknownHostException;

@SpringBootApplication
public class ServletDemoServer {

    private static final Logger log = LoggerFactory.getLogger(ServletDemoServer.class);

    public static void main(String[] args) throws UnknownHostException {

        SpringApplication app = new SpringApplication(ServletDemoServer.class);

        Environment env = app.run(args).getEnvironment();

        log.info("Access URLs:" +
                        "\n----------------------------------------------------------\n\t" +
                        "External: \thttp://{}:{}\n\t" +
                        "\n----------------------------------------------------------",
                InetAddress.getLocalHost().getHostAddress(),
                env.getProperty("server.port")
        );
    }
}
