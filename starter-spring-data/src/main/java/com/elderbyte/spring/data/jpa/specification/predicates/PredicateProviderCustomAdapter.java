package com.elderbyte.spring.data.jpa.specification.predicates;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public class PredicateProviderCustomAdapter<T> implements PredicateProvider<T> {

    private final String value;
    private final CustomPredicateProvider<T> customPredicateProvider;

    public PredicateProviderCustomAdapter(String value, CustomPredicateProvider<T> customPredicateProvider){
        this.value = value;
        this.customPredicateProvider = customPredicateProvider;
    }

    @Override
    public Predicate getPredicate(Root<T> root, CriteriaBuilder cb) {
        return customPredicateProvider.getPredicate(root, cb, value);
    }
}
