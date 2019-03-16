package com.elderbyte.spring.boot.bootstrap;


import com.elderbyte.spring.boot.bootstrap.jackson.DefaultJacksonConfiguration;
import com.elderbyte.spring.boot.bootstrap.reactive.ReactiveWebSupportConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({
        DefaultJacksonConfiguration.class,
        ReactiveWebSupportConfiguration.class
        })
public class SpringWebFluxBootstrapAutoConfiguration {


}
