package com.elderbyte.spring.data.mongo.queries;

import org.bson.Document;
import org.springframework.data.mongodb.core.query.CriteriaDefinition;
import org.springframework.data.mongodb.core.query.Query;

public class QueryCriteria implements CriteriaDefinition {

    private final Query query;

    public QueryCriteria(Query query){
        this.query = query;
    }

    @Override
    public Document getCriteriaObject() {
        return query.getQueryObject();
    }

    @Override
    public String getKey() {
        return null;
    }
}
