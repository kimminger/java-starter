package com.elderbyte.spring.data.jpa.specification.predicates;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public class PredicateProviderPathValueAdapter<T> implements PredicateProvider<T> {

    private final String path;
    private final String value;

    private final PredicateProviderPathValue<T> predicateBuildStrategy;

    public PredicateProviderPathValueAdapter(String path, String value, PredicateProviderPathValue<T> predicateBuildStrategy){
        this.path = path;
        this.value = value;
        this.predicateBuildStrategy = predicateBuildStrategy;
    }

    @Override
    public Predicate getPredicate(Root<T> root, CriteriaBuilder cb) {
        return predicateBuildStrategy.buildPredicate(root, cb, path, value);
    }


    @Override
    public String toString() {
        return "PredicateProviderPathValueAdapter{" +
                "path='" + path + '\'' +
                ", value='" + value + '\'' +
                ", predicateBuildStrategy=" + predicateBuildStrategy +
                '}';
    }
}
