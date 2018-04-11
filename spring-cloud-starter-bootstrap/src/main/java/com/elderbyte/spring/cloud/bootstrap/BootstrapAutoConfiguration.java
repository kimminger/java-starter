package com.elderbyte.spring.cloud.bootstrap;


import com.elderbyte.commons.data.contiunation.DataConfiguration;
import com.elderbyte.spring.cloud.bootstrap.feign.DefaultFeignConfiguration;
import com.elderbyte.spring.cloud.bootstrap.jackson.DefaultJacksonConfiguration;
import com.elderbyte.spring.cloud.bootstrap.support.RestGenericExceptionHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({
        DefaultFeignConfiguration.class,
        DefaultJacksonConfiguration.class,
        RestGenericExceptionHandler.class,
        DataConfiguration.class
        })
public class BootstrapAutoConfiguration {


}
