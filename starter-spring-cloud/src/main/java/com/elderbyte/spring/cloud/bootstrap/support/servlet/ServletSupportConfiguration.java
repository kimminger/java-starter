package com.elderbyte.spring.cloud.bootstrap.support.servlet;

import com.elderbyte.spring.cloud.bootstrap.conditions.ServletWebApplicationCondition;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Conditional(ServletWebApplicationCondition.class)
@Import({
        ServletGenericExceptionHandler.class,
})
public class ServletSupportConfiguration {
}
