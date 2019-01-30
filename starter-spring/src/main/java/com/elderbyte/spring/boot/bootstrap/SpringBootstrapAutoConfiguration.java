package com.elderbyte.spring.boot.bootstrap;


import com.elderbyte.spring.boot.bootstrap.data.DataConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({
        DataConfiguration.class
        })
public class SpringBootstrapAutoConfiguration {


}
