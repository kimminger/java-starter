package com.elderbyte.spring.data.jpa.specification.predicates;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public interface PredicateProviderExpression<T> {
    /**
     * Builds a predicate restriction
     *
     * @param cb The cirteria-builder factory
     * @param path The path to wire the predicate against
     * @return A predicate representing this restriction
     */
    Predicate buildPredicate(Root<T> root, CriteriaBuilder cb, Expression<?> path);
}
