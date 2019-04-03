package com.elderbyte.spring.bootstrap;

import com.elderbyte.spring.bootstrap.servlet.ServletSupportConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({
        ServletSupportConfiguration.class,
        })
public class SpringServletBootstrapAutoConfiguration {

}
