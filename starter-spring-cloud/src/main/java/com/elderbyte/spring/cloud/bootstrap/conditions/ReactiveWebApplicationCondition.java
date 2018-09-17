package com.elderbyte.spring.cloud.bootstrap.conditions;

import org.springframework.boot.WebApplicationType;

public class ReactiveWebApplicationCondition extends WebApplicationTypeCondition {
    public ReactiveWebApplicationCondition() {
        super(WebApplicationType.REACTIVE);
    }
}