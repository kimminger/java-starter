package com.elderbyte.spring.data.jpa.specification.predicates.support;


import com.elderbyte.spring.data.jpa.specification.JpaPath;
import com.elderbyte.spring.data.jpa.specification.MatchablePredicateBuildStrategy;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.math.BigDecimal;

public class BigDecimalPredicateBuildStrategy<T>  implements MatchablePredicateBuildStrategy<T> {

    @Override
    public boolean canHandle(Root<T> root, String pathExpression, String value) {
        var path = JpaPath.resolve(root, pathExpression);
        return (BigDecimal.class.isAssignableFrom(path.getJavaType()));
    }

    @Override
    public Predicate buildPredicate(Root<T> root, CriteriaBuilder cb, String pathExpression, String value) {
        var path = JpaPath.resolve(root, pathExpression);
        final double doubleValue = Double.parseDouble(value);
        return cb.equal(path, BigDecimal.valueOf(doubleValue));
    }
}

