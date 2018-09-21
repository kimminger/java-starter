package com.elderbyte.spring.data.jpa.specification.predicates.support;


import com.elderbyte.spring.data.jpa.specification.JpaPathExpression;
import com.elderbyte.spring.data.jpa.specification.MatchablePredicateBuildStrategy;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.math.BigDecimal;

public class BigDecimalPredicateBuildStrategy  implements MatchablePredicateBuildStrategy {

    @Override
    public boolean canHandle(Root<?> root, String pathExpression, String value) {
        var path = JpaPathExpression.resolve(root, pathExpression);
        return (BigDecimal.class.isAssignableFrom(path.getJavaType()));
    }

    @Override
    public Predicate buildPredicate(Root<?> root, String pathExpression, CriteriaBuilder criteriaBuilder, String value) {
        var path = JpaPathExpression.resolve(root, pathExpression);
        final double doubleValue = Double.parseDouble(value);
        return criteriaBuilder.equal(path, BigDecimal.valueOf(doubleValue));
    }
}

