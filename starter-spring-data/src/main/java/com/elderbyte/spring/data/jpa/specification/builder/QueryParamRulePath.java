package com.elderbyte.spring.data.jpa.specification.builder;

public class QueryParamRulePath<T> implements QueryParamRule<T> {

    private final String path;

    public QueryParamRulePath(String path){
        this.path = path;
    }

    public String getPath() {
        return path;
    }
}
