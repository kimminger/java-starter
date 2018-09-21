package com.elderbyte.spring.data.jpa.specification.builder;

import com.elderbyte.spring.data.jpa.specification.predicates.CustomPredicateProvider;

public class QueryParamRuleCustomPredicate<T> implements QueryParamRule<T> {

    private final CustomPredicateProvider<T> customPredicateProvider;

    public QueryParamRuleCustomPredicate(CustomPredicateProvider<T> customPredicateProvider) {
        this.customPredicateProvider = customPredicateProvider;
    }


    public CustomPredicateProvider<T> getCustomPredicateProvider() {
        return customPredicateProvider;
    }
}
