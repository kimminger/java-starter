package com.elderbyte.spring.cloud.bootstrap;


import com.elderbyte.spring.cloud.bootstrap.exceptions.RestGenericExceptionHandler;
import com.elderbyte.spring.cloud.bootstrap.feign.DefaultFeignConfiguration;
import com.elderbyte.spring.cloud.bootstrap.jackson.DefaultJacksonConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({
        DefaultFeignConfiguration.class,
        DefaultJacksonConfiguration.class,
        RestGenericExceptionHandler.class
        })
public class BootstrapAutoConfiguration {


}
