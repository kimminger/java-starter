package com.elderbyte.spring.data.jpa.specification.predicates;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

@FunctionalInterface
public interface CustomPredicateProvider<T> {

    Predicate getPredicate(Root<T> root, CriteriaBuilder cb, String value);

}
