package com.elderbyte.spring.data.jpa.specification.builder;

import java.util.*;

public class QueryParamsBuilder {

    private Map<String, Set<String>> multiValueParams = new HashMap<>();

    public static QueryParamsBuilder start(){
        return new QueryParamsBuilder();
    }

    public static QueryParamsBuilder from(Map<String, ? extends Collection<String>> paramMap){
        var builder = start();
        paramMap.forEach(builder::add);
        return builder;
    }

    public QueryParamsBuilder merge(Map<String, String[]> paramMap){
        paramMap.forEach(this::add);
        return this;
    }

    public QueryParamsBuilder add(String key, String[] newValues){
        return add(key, Arrays.asList(newValues));
    }

    public QueryParamsBuilder add(String key, Collection<String> newValues){
        var values = multiValueParams.computeIfAbsent(key, (a) -> new HashSet<>());
        values.addAll(newValues);
        return this;
    }

    public QueryParamsBuilder add(String key, String value){
        var values = multiValueParams.computeIfAbsent(key, (a) -> new HashSet<>());
        values.add(value);
        return this;
    }

    public Map<String, Set<String>> build(){
        return multiValueParams;
    }

}
