package com.elderbyte.spring.data.jpa.specification.predicates.support;


import com.elderbyte.commons.data.enums.ValueEnum;
import com.elderbyte.spring.data.jpa.specification.JpaPath;
import com.elderbyte.spring.data.jpa.specification.MatchablePredicateBuildStrategy;

import javax.persistence.criteria.*;
import java.util.Arrays;

import static java.util.stream.Collectors.toList;

public class EnumPredicateBuildStrategy<T> implements MatchablePredicateBuildStrategy<T> {
    @Override
    public boolean canHandle(Root<T> root, String pathExpression, String value) {
        var path = JpaPath.resolve(root, pathExpression);
        return (Enum.class.isAssignableFrom(path.getJavaType()));
    }

    @Override
    public Predicate buildPredicate(Root<T> root, CriteriaBuilder cb, String pathExpression, String value) {
        Expression<? extends Enum> path = JpaPath.resolve(root, pathExpression);
        return buildEnumPredicate(path, cb, value);
    }

    private Predicate buildEnumPredicate(Expression<? extends Enum> path, CriteriaBuilder criteriaBuilder, String value) {

        if (!ValueEnum.class.isAssignableFrom(path.getJavaType()))
            return StringPredicateBuilder.matchInPlace(path.as(String.class), criteriaBuilder, value);

        var constants = path.getJavaType().getEnumConstants();
        var keyMatchers = Arrays.stream(constants)
                .filter(constant -> constant.name().toLowerCase().contains(value.toLowerCase()))
                .map(item -> StringPredicateBuilder.equalIgnoreCase(path.as(String.class), criteriaBuilder, ((ValueEnum<?>) item).getValue()))
                .collect(toList());

        var keysPredicate = criteriaBuilder.or(keyMatchers.toArray(new Predicate[]{}));

        return criteriaBuilder.or(
                keysPredicate,
                StringPredicateBuilder.matchInPlace(path.as(String.class), criteriaBuilder, value)
        );
    }
}
