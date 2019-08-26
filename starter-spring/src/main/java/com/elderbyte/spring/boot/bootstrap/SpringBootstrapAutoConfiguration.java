package com.elderbyte.spring.boot.bootstrap;


import com.elderbyte.spring.boot.bootstrap.data.DataConfiguration;
import com.elderbyte.spring.boot.bootstrap.metrics.MicrometerConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({
        DataConfiguration.class,
        MicrometerConfiguration.class
})
public class SpringBootstrapAutoConfiguration {


}
