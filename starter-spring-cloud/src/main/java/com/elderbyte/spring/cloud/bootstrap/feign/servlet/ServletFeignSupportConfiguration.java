package com.elderbyte.spring.cloud.bootstrap.feign.servlet;

import com.elderbyte.spring.boot.bootstrap.conditions.ServletWebApplicationCondition;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Conditional(ServletWebApplicationCondition.class)
@Import({
        ServletFeignExceptionHandler.class,
})
public class ServletFeignSupportConfiguration {
}
