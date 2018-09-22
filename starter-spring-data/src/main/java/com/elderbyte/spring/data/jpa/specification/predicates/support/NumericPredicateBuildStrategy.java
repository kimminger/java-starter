package com.elderbyte.spring.data.jpa.specification.predicates.support;

import com.elderbyte.commons.utils.NumberUtil;
import com.elderbyte.spring.data.jpa.specification.JpaPath;
import com.elderbyte.spring.data.jpa.specification.MatchablePredicateBuildStrategy;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public class NumericPredicateBuildStrategy<T> implements MatchablePredicateBuildStrategy<T> {

    @Override
    public boolean canHandle(Root<T> root, String pathExpression, String value) {
        var path = JpaPath.resolve(root, pathExpression);
        return NumberUtil.isNumeric(path.getJavaType());
    }

    @Override
    public Predicate buildPredicate(Root<T> root, CriteriaBuilder cb, String pathExpression, String value) {
        Expression<? extends Number> path = JpaPath.resolve(root, pathExpression);
        var type = path.getJavaType();

        // TODO Support expressions

        var numberValue = NumberUtil.parseNumber(value, type);
        return cb.equal(path, numberValue);
    }

    

}
