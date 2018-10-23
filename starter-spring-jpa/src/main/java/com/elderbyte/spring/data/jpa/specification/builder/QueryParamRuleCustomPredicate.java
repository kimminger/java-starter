package com.elderbyte.spring.data.jpa.specification.builder;

import com.elderbyte.spring.data.jpa.specification.predicates.PredicateProviderValue;

public class QueryParamRuleCustomPredicate<T> implements QueryParamRule<T> {

    private final PredicateProviderValue<T> customPredicateProvider;

    public QueryParamRuleCustomPredicate(PredicateProviderValue<T> customPredicateProvider) {
        this.customPredicateProvider = customPredicateProvider;
    }

    public PredicateProviderValue<T> getPredicateProviderValue() {
        return customPredicateProvider;
    }
}
