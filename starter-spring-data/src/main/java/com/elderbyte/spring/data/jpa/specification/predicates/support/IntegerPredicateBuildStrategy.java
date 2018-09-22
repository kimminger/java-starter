package com.elderbyte.spring.data.jpa.specification.predicates.support;

import com.elderbyte.spring.data.jpa.specification.JpaPath;
import com.elderbyte.spring.data.jpa.specification.MatchablePredicateBuildStrategy;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public class IntegerPredicateBuildStrategy<T> implements MatchablePredicateBuildStrategy<T> {


    @Override
    public boolean canHandle(Root<T> root, String pathExpression, String value) {
        var path = JpaPath.resolve(root, pathExpression);
        return (Integer.class.isAssignableFrom(path.getJavaType()) || int.class.isAssignableFrom(path.getJavaType()));
    }

    @Override
    public Predicate buildPredicate(Root<T> root, CriteriaBuilder cb, String pathExpression, String value) {
        var path = JpaPath.resolve(root, pathExpression);
        final int intValue = Integer.parseInt(value);
        return cb.equal(path, intValue);
    }
}
