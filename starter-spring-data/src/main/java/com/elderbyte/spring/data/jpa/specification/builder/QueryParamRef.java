package com.elderbyte.spring.data.jpa.specification.builder;

import java.util.Optional;

public class QueryParamRef {

    private final String key;
    private final boolean required;
    private final String defaultValue;

    public QueryParamRef(String key) {
        this(key, false, null);
    }

    public QueryParamRef(String key, boolean required, String defaultValue) {
        this.key = key;
        this.required = required;
        this.defaultValue = defaultValue;
    }

    public String getKey() {
        return key;
    }

    public boolean isRequired() {
        return required;
    }

    public Optional<String> getDefaultValue() {
        return Optional.ofNullable(defaultValue);
    }
}
