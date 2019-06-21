package com.elderbyte.spring.data.mongo.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.lang.Nullable;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@NoRepositoryBean
public interface EntityMongoRepository<T, ID extends Serializable> extends MongoRepository<T, ID> {

    /***************************************************************************
     *                                                                         *
     * Query Multiple                                                          *
     *                                                                         *
     **************************************************************************/

    Page<T> findAll(Query query, Pageable pageable);

    List<T> findAll(@Nullable Query query);

    /***************************************************************************
     *                                                                         *
     * Count                                                                   *
     *                                                                         *
     **************************************************************************/

    long count(Query query);

    /***************************************************************************
     *                                                                         *
     * Partial Update                                                          *
     *                                                                         *
     **************************************************************************/

    T partialSave(T entity, String... includeProperties);

    T partialSave(T entity, ID id, Collection<String> includeProperties);

    T partialSave(ID id, String property, Object value);

    T partialSave(ID id, Map<String, Object> updateValues);

}
