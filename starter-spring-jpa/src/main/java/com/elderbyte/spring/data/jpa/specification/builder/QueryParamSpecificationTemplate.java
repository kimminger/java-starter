package com.elderbyte.spring.data.jpa.specification.builder;

import com.elderbyte.commons.exceptions.ArgumentNullException;
import com.elderbyte.commons.exceptions.NotSupportedException;
import com.elderbyte.spring.data.jpa.specification.expressions.Expression;
import com.elderbyte.spring.data.jpa.specification.expressions.LogicExpression;
import com.elderbyte.spring.data.jpa.specification.expressions.ValueExpression;
import com.elderbyte.spring.data.jpa.specification.predicates.*;

import java.util.*;

public class QueryParamSpecificationTemplate<T> {

    /***************************************************************************
     *                                                                         *
     * Fields                                                                  *
     *                                                                         *
     **************************************************************************/

    private final PredicateProviderPathValue<T> defaultStrategy;
    private final Map<String, QueryParamCriteria<T>> rules = new HashMap<>();

    /***************************************************************************
     *                                                                         *
     * Constructor                                                             *
     *                                                                         *
     **************************************************************************/

    public QueryParamSpecificationTemplate(Collection<QueryParamCriteria<T>> rules, PredicateProviderPathValue<T> defaultStrategy){

        if(rules == null) throw new ArgumentNullException("rules");
        if(defaultStrategy == null) throw new ArgumentNullException("defaultStrategy");

        this.defaultStrategy = defaultStrategy;
        rules.forEach(r -> this.rules.put(r.getQueryParam().getKey(), r));
    }

    /***************************************************************************
     *                                                                         *
     * Public API                                                              *
     *                                                                         *
     **************************************************************************/

    /**
     * Resolve a search criteria expression derived from the given rules and provided query params.
     * @param queryParams
     * @return Returns a predicate-provider expression - or null if no predicates are available
     */
    public Expression<PredicateProvider<T>> resolve(Map<String, ? extends Collection<String>> queryParams){

        // TODO Also support default params

        List<Expression<PredicateProvider<T>>> conjunction = new ArrayList<>();

        for(var param : queryParams.entrySet()){
            var rule = rules.get(param.getKey());
            if(rule != null){
                for(var v : param.getValue()){
                    conjunction.add(applyRule(rule, v));
                }
            }
        }

        return LogicExpression.conjunction(conjunction);
    }

    /***************************************************************************
     *                                                                         *
     * Private methods                                                         *
     *                                                                         *
     **************************************************************************/

    private Expression<PredicateProvider<T>> applyRule(QueryParamCriteria<T> queryParamCriteria, String value){

        var ruleExpression = queryParamCriteria.getRuleExpression();

        return mapExpression(ruleExpression, value);
    }

    private Expression<PredicateProvider<T>> mapExpression(Expression<QueryParamRule<T>> ruleExpression, String value){

        if(ruleExpression == null){
            return null;
        }else if(ruleExpression instanceof ValueExpression){
            var valueExpr = (ValueExpression<QueryParamRule<T>>)ruleExpression;
            PredicateProvider<T> singleProvider = resolveRule(valueExpr.getValue(), value);
            return new ValueExpression<>(singleProvider);
        }else if(ruleExpression instanceof LogicExpression){

            var logicExpr = (LogicExpression<QueryParamRule<T>>)ruleExpression;

            var left = mapExpression(logicExpr.getLeft(), value);
            var right = mapExpression(logicExpr.getRight(), value);

            return new LogicExpression<>(left, logicExpr.getOperator(), right);
        }else{
            throw new NotSupportedException("Unexpected / Unsupported expression: " + ruleExpression);
        }
    }

    private PredicateProvider<T> resolveRule(QueryParamRule<T> rule, String value){

        if(rule instanceof QueryParamRulePath) {
            var dynamicPathRule = ((QueryParamRulePath) rule);
            return new PredicateProviderPathValueAdapter<>(dynamicPathRule.getPath(), value, defaultStrategy);
        }else if(rule instanceof QueryParamRuleCustomPredicate){
            var customPredicateRule = ((QueryParamRuleCustomPredicate<T>) rule);
            return new PredicateProviderValueAdapter<>(value, customPredicateRule.getPredicateProviderValue());
        }else{
            throw new NotSupportedException("The given rule is not supported: " + rule);
        }
    }


}
