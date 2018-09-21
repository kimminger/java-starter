package com.elderbyte.spring.data.jpa.specification.predicates.support;


import com.elderbyte.commons.data.enums.ValueEnum;
import com.elderbyte.spring.data.jpa.specification.JpaPathExpression;
import com.elderbyte.spring.data.jpa.specification.MatchablePredicateBuildStrategy;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.Arrays;

import static java.util.stream.Collectors.toList;

public class EnumPredicateBuildStrategy implements MatchablePredicateBuildStrategy {
    @Override
    public boolean canHandle(Root<?> root, String pathExpression, String value) {
        var path = JpaPathExpression.resolve(root, pathExpression);
        return (Enum.class.isAssignableFrom(path.getJavaType()));
    }

    @Override
    public Predicate buildPredicate(Root<?> root, String pathExpression, CriteriaBuilder cb, String value) {
        var path = JpaPathExpression.resolve(root, pathExpression);
        return buildEnumPredicate((Path<? extends Enum>)path, cb, value);
    }

    private Predicate buildEnumPredicate(Path<? extends Enum> path, CriteriaBuilder criteriaBuilder, String value) {

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
