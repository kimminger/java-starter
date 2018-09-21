package com.elderbyte.spring.data.jpa.specification.predicates.support;

import com.elderbyte.commons.exceptions.NotSupportedException;
import com.elderbyte.spring.data.jpa.specification.JpaPathExpression;
import com.elderbyte.spring.data.jpa.specification.MatchablePredicateBuildStrategy;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public class NullPredicateBuildStrategy<T> implements MatchablePredicateBuildStrategy<T> {

    @Override
    public boolean canHandle(Root<T> root, String pathExpression, String value) {
       return value.startsWith("$eq ");
    }

    @Override
    public Predicate buildPredicate(Root<T> root, CriteriaBuilder cb, String pathExpression, String value) {
        var path = JpaPathExpression.resolve(root, pathExpression);
        switch (value) {
            case "$eq null":
                return cb.isNull(path);
            case "$eq null or empty":
                return cb.or(cb.isNull(path), cb.equal(path, ""));
            case "$eq not null":
                return cb.isNotNull(path);
            case "$eq not null or empty":
                return cb.and(cb.isNotNull(path), cb.notEqual(path, ""));
            default:
                throw new NotSupportedException("The given value expression is not supported: '" + value + "'");
        }
    }
}
