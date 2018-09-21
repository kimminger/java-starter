package com.elderbyte.spring.data.jpa.specification.predicates;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public interface PredicateBuildStrategy {
    /**
     * Builds a predicate restriction
     *
     * @param pathExpression The path to wire the predicate against
     * @param criteriaBuilder The cirteria-builder factory
     * @param value The value to check against the path
     * @return A predicate representing this restriction
     */
    Predicate buildPredicate(Root<?> root, String pathExpression, CriteriaBuilder criteriaBuilder, String value);
}
