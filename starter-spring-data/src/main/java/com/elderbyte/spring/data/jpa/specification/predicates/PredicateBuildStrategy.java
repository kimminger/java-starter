package com.elderbyte.spring.data.jpa.specification.predicates;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public interface PredicateBuildStrategy<T> {
    /**
     * Builds a predicate restriction
     *
     * @param pathExpression The path to wire the predicate against
     * @param cb The cirteria-builder factory
     * @param value The value to check against the path
     * @return A predicate representing this restriction
     */
    Predicate buildPredicate(Root<T> root,  CriteriaBuilder cb, String pathExpression, String value);
}
