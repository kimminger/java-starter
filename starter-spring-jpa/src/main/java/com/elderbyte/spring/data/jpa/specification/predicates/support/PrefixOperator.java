package com.elderbyte.spring.data.jpa.specification.predicates.support;

import com.elderbyte.commons.data.enums.ValueEnum;

public enum PrefixOperator implements ValueEnum<String> {

    GREATER_THAN_OR_EQUAL(">="),
    LESS_THAN_OR_EQUAL("<="),


    NOT_EQUAL("!"),
    GREATER_THAN(">"),
    LESS_THAN("<"),

    NONE(""),
    ;

    private final String value;

    PrefixOperator(String value){
        this.value = value;
    }

    @Override
    public String getValue() {
        return value;
    }
}
