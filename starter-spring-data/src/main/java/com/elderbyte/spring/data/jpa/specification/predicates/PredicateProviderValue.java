package com.elderbyte.spring.data.jpa.specification.predicates;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public interface PredicateProviderValue<T> {
    /**
     * Builds a predicate restriction
     *
     * @param cb The cirteria-builder factory
     * @param value The value argument to consider in the check
     * @return A predicate representing this restriction
     */
    Predicate buildPredicate(Root<T> root, CriteriaBuilder cb, String value);
}
