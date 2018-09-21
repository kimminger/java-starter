package com.elderbyte.spring.data.jpa.specification.builder;

import com.elderbyte.spring.data.jpa.specification.predicates.PredicateBuildStrategy;

public class QueryParamRuleCustomPredicate<T> implements QueryParamRule<T> {

    private final String path;
    private final PredicateBuildStrategy<T> customPredicateProvider;

    public QueryParamRuleCustomPredicate(String path, PredicateBuildStrategy<T> customPredicateProvider) {
        this.path = path;
        this.customPredicateProvider = customPredicateProvider;
    }


    public PredicateBuildStrategy<T> getCustomPredicateProvider() {
        return customPredicateProvider;
    }

    public String getPath() {
        return path;
    }
}
