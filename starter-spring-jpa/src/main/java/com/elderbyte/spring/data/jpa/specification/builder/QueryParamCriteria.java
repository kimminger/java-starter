package com.elderbyte.spring.data.jpa.specification.builder;

import com.elderbyte.commons.exceptions.ArgumentNullException;
import com.elderbyte.spring.data.jpa.specification.expressions.Expression;

public class QueryParamCriteria<T> {

    /***************************************************************************
     *                                                                         *
     * Fields                                                                  *
     *                                                                         *
     **************************************************************************/

    private final QueryParamRef queryParam;

    private final Expression<QueryParamRule<T>> rule;

    /***************************************************************************
     *                                                                         *
     * Constructor                                                             *
     *                                                                         *
     **************************************************************************/

    public QueryParamCriteria(QueryParamRef queryParam, Expression<QueryParamRule<T>> rule){
        if(queryParam == null) throw new ArgumentNullException("queryParam");
        this.queryParam = queryParam;
        this.rule = rule;
    }

    /***************************************************************************
     *                                                                         *
     * Properties                                                              *
     *                                                                         *
     **************************************************************************/

    public QueryParamRef getQueryParam() {
        return queryParam;
    }

    public Expression<QueryParamRule<T>> getRuleExpression() {
        return rule;
    }
}
