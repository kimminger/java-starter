package com.elderbyte.spring.cloud.bootstrap.support.reactive;

import com.elderbyte.spring.cloud.bootstrap.support.reactive.pagination.ReactivePageableHandlerMethodArgumentResolver;
import com.elderbyte.spring.cloud.bootstrap.support.reactive.pagination.ReactiveSortHandlerMethodArgumentResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.config.WebFluxConfigurer;
import org.springframework.web.reactive.result.method.annotation.ArgumentResolverConfigurer;

@Deprecated
@Configuration
public class DefaultWebfluxConfigurer implements WebFluxConfigurer {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public void configureArgumentResolvers(ArgumentResolverConfigurer configurer) {

        logger.info("Configuring pageable / sort webflux param support!");

        configurer.addCustomResolver(
                new ReactiveSortHandlerMethodArgumentResolver(),
                new ReactivePageableHandlerMethodArgumentResolver()
        );
    }
}
