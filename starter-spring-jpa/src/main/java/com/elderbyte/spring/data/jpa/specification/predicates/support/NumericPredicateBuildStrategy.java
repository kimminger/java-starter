package com.elderbyte.spring.data.jpa.specification.predicates.support;

import com.elderbyte.commons.exceptions.NotSupportedException;
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
    public Predicate buildPredicate(Root<T> root, CriteriaBuilder cb, String pathExpression, String rawValue) {
        Expression<? extends Number> path = JpaPath.resolve(root, pathExpression);
        var type = path.getJavaType();

        var op = getOperator(rawValue);
        var value = stripOperator(rawValue, op);

        Comparable numberValue = (Comparable) NumberUtil.parseNumber(value, type);

        switch (op){
            case NONE: return cb.equal(path, numberValue);
            case NOT_EQUAL: return cb.notEqual(path, numberValue);
            case LESS_THAN: return cb.lessThan((Expression)path, numberValue);
            case LESS_THAN_OR_EQUAL: return cb.lessThanOrEqualTo((Expression)path, numberValue);
            case GREATER_THAN: return cb.greaterThan((Expression)path, numberValue);
            case GREATER_THAN_OR_EQUAL: return cb.greaterThanOrEqualTo((Expression)path, numberValue);

            default:
                throw new NotSupportedException("Unknown operator: " + op);
        }

    }

    private PrefixOperator getOperator(String rawValue){
        for(var op : PrefixOperator.values()){
            if(op != PrefixOperator.NONE){
                if(rawValue.startsWith(op.getValue())){
                    return op;
                }
            }
        }
        return PrefixOperator.NONE;
    }

    private String stripOperator(String value, PrefixOperator op){
        if(op == PrefixOperator.NONE) return value;
        return value.substring(op.getValue().length());
    }
}
