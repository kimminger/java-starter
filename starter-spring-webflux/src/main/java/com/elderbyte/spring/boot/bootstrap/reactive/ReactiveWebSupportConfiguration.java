package com.elderbyte.spring.boot.bootstrap.reactive;

import com.elderbyte.spring.boot.bootstrap.conditions.ReactiveWebApplicationCondition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Conditional(ReactiveWebApplicationCondition.class)
@Import({
        ReactiveGenericExceptionHandler.class
})
public class ReactiveWebSupportConfiguration {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    public ReactiveWebSupportConfiguration(){
    }

}
