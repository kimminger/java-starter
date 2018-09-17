package com.elderbyte.spring.cloud.bootstrap.support.reactive;

import com.elderbyte.spring.cloud.bootstrap.conditions.ReactiveWebApplicationCondition;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Conditional(ReactiveWebApplicationCondition.class)
@Import({
        ReactiveGenericExceptionHandler.class
})
public class ReactiveWebSupportConfiguration {
}
