package com.elderbyte.spring.data.jpa.specification.predicates;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public class DynamicPredicateProvider<T> implements PredicateProvider<T> {

    private final String path;
    private final String value;

    private final PredicateBuildStrategy predicateBuildStrategy;


    public DynamicPredicateProvider(String path, String value, PredicateBuildStrategy predicateBuildStrategy){
        this.path = path;
        this.value = value;
        this.predicateBuildStrategy = predicateBuildStrategy;
    }

    @Override
    public Predicate getPredicate(Root<T> root, CriteriaBuilder cb) {
        return predicateBuildStrategy.buildPredicate(root, path, cb, value);
    }
}
