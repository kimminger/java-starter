package com.elderbyte.spring.data.jpa.specification.predicates.support;

import com.elderbyte.spring.data.jpa.specification.JpaPathExpression;
import com.elderbyte.spring.data.jpa.specification.MatchablePredicateBuildStrategy;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public class BooleanPredicateBuildStrategy<T> implements MatchablePredicateBuildStrategy<T> {

    @Override
    public boolean canHandle(Root<T> root, String pathExpression, String value) {
        var path = JpaPathExpression.resolve(root, pathExpression);
        return (Boolean.class.isAssignableFrom(path.getJavaType()) || boolean.class.isAssignableFrom(path.getJavaType()));
    }

    @Override
    public Predicate buildPredicate(Root<T> root, CriteriaBuilder cb, String pathExpression, String value) {
        var path = JpaPathExpression.resolve(root, pathExpression);
        final boolean booleanValue = Boolean.parseBoolean(value);
        return cb.equal(path, booleanValue);
    }
}
