package com.elderbyte.spring.cloud.bootstrap;


import com.elderbyte.spring.cloud.bootstrap.feign.DefaultFeignConfiguration;
import com.elderbyte.spring.cloud.bootstrap.support.reactive.ReactiveDecoderConfiguration;
import com.elderbyte.spring.cloud.bootstrap.support.servlet.ServletSupportConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({
        DefaultFeignConfiguration.class,
        ServletSupportConfiguration.class,
        ReactiveDecoderConfiguration.class
        })
public class SpringCloudBootstrapAutoConfiguration {


}
