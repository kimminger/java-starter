package com.elderbyte.spring.data.jpa.specification.predicates;

import com.elderbyte.commons.exceptions.NotSupportedException;
import com.elderbyte.spring.data.jpa.specification.expressions.Expression;
import com.elderbyte.spring.data.jpa.specification.expressions.LogicExpression;
import com.elderbyte.spring.data.jpa.specification.expressions.ValueExpression;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

/**
 * Provides the ability to newSpec a search specification from a search criteria expression.
 * @param <T> The entity type.
 */
public class PredicateExpressionSpecification<T> implements Specification<T>  {


    /***************************************************************************
     *                                                                         *
     * Fields                                                                  *
     *                                                                         *
     **************************************************************************/

    private static final Logger logger = LoggerFactory.getLogger(PredicateExpressionSpecification.class);

    private final Expression<PredicateProvider<T>> predicateExpression;

    /***************************************************************************
     *                                                                         *
     * Constructor                                                             *
     *                                                                         *
     **************************************************************************/

    public PredicateExpressionSpecification(Expression<PredicateProvider<T>> criteriaExpression) {
        this.predicateExpression = criteriaExpression;
    }

    /***************************************************************************
     *                                                                         *
     * Public API                                                              *
     *                                                                         *
     **************************************************************************/

    @Override
    public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        return apply(predicateExpression, root, cb);
    }

    @Override
    public String toString() {
        return "PredicateExpressionSpecification{" +
                "predicateExpression=" + predicateExpression +
                '}';
    }

    /***************************************************************************
     *                                                                         *
     * Private methods                                                         *
     *                                                                         *
     **************************************************************************/

    private Predicate apply(Expression<PredicateProvider<T>> expression, Root<T> root, CriteriaBuilder cb){

        if(expression == null){
            return null;
        }else if(expression instanceof ValueExpression){
            var criteria = ((ValueExpression<PredicateProvider<T>>) expression).getValue();
            return criteria.getPredicate(root, cb);
        }else if (expression instanceof LogicExpression){
            var crit = ((LogicExpression<PredicateProvider<T>>) expression);
            var left = apply(crit.getLeft(), root, cb);
            var rigth = apply(crit.getRight(), root, cb);

            switch (crit.getOperator()){
                case AND: return cb.and(left, rigth);
                case OR: return cb.or(left, rigth);
                default: throw new NotSupportedException("Unexpected logic operator: " + crit.getOperator());
            }
        }else{
            throw new NotSupportedException("Unexpected criteria expression: " + expression);
        }
    }

}
