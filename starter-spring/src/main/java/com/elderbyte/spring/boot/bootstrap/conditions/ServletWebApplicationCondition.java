package com.elderbyte.spring.boot.bootstrap.conditions;

import org.springframework.boot.WebApplicationType;

public class ServletWebApplicationCondition extends WebApplicationTypeCondition {
    public ServletWebApplicationCondition() {
        super(WebApplicationType.SERVLET);
    }
}
