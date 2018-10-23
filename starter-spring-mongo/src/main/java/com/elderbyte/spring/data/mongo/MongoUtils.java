package com.elderbyte.spring.data.mongo;

import com.elderbyte.spring.data.mongo.queries.QueryCriteria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.CriteriaDefinition;
import org.springframework.data.mongodb.core.query.Query;


public final class MongoUtils {

    public static <T> Page<T> findAllPaged(MongoOperations mongoOperations, Query query, Class<T> clazz, Pageable pageable){
        query.with(pageable);
        Long count = mongoOperations.count(query, clazz);
        var list = mongoOperations.find(query, clazz);
        return new PageImpl<>(list, pageable, count);
    }

    public static CriteriaDefinition criteria(Query query){
        return new QueryCriteria(query);
    }

}