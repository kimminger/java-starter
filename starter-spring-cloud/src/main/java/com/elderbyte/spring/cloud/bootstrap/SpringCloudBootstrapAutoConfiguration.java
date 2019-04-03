package com.elderbyte.spring.cloud.bootstrap;


import com.elderbyte.spring.cloud.bootstrap.feign.DefaultFeignConfiguration;
import com.elderbyte.spring.cloud.bootstrap.feign.servlet.ServletFeignSupportConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({
        DefaultFeignConfiguration.class,
        ServletFeignSupportConfiguration.class
        })
public class SpringCloudBootstrapAutoConfiguration {


}
