package com.elderbyte.spring.data.jpa.specification.predicates;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public class PredicateProviderValueAdapter<T> implements PredicateProvider<T> {

    private final String value;
    private final PredicateProviderValue<T> predicateBuildStrategy;

    public PredicateProviderValueAdapter(String value, PredicateProviderValue<T> predicateBuildStrategy){
        this.value = value;
        this.predicateBuildStrategy = predicateBuildStrategy;
    }

    @Override
    public Predicate getPredicate(Root<T> root, CriteriaBuilder cb) {
        return predicateBuildStrategy.buildPredicate(root, cb, value);
    }


    @Override
    public String toString() {
        return "PredicateProviderPathValueAdapter{" +
                ", value='" + value + '\'' +
                ", predicateBuildStrategy=" + predicateBuildStrategy +
                '}';
    }
}
