package com.elderbyte.spring.data.mongo.repositories;

import com.elderbyte.commons.exceptions.ArgumentNullException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.mongodb.repository.query.MongoEntityInformation;
import org.springframework.data.mongodb.repository.support.MappingMongoEntityInformation;
import org.springframework.data.mongodb.repository.support.SimpleMongoRepository;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.support.PageableExecutionUtils;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.*;

import static java.util.stream.Collectors.toMap;
import static org.springframework.data.mongodb.core.query.Criteria.where;

@NoRepositoryBean
public class EntityMongoRepositoryImpl<T, ID extends Serializable>
        extends SimpleMongoRepository<T, ID> implements EntityMongoRepository<T, ID> {

    /***************************************************************************
     *                                                                         *
     * Fields                                                                  *
     *                                                                         *
     **************************************************************************/

    private final MongoOperations mongoOperations;
    private final MongoEntityInformation<T, ID> metadata;

    private final Map<Class, Map<String, Field>> fieldIndex = new HashMap<>();

    /***************************************************************************
     *                                                                         *
     * Constructors                                                            *
     *                                                                         *
     **************************************************************************/

    public EntityMongoRepositoryImpl(
            MappingMongoEntityInformation<T, ID> metadata,
            MongoTemplate mongoOperations
    ) {
        super(metadata, mongoOperations);
        this.mongoOperations = mongoOperations;
        this.metadata = metadata;
    }

    /***************************************************************************
     *                                                                         *
     * Public API                                                              *
     *                                                                         *
     **************************************************************************/

    @Override
    public Page<T> findAll(Query query, Pageable pageable) {
        query.with(pageable);
        long count = count(query);
        var list = findAll(query);

        return PageableExecutionUtils.getPage(
                list,
                pageable,
                () -> count
        );
    }

    @Override
    public List<T> findAll(Query query) {
        if (query == null) {
            return Collections.emptyList();
        }
        return this.mongoOperations.find(query, metadata.getJavaType());
    }

    @Override
    public long count(Query query) {
        return mongoOperations.count(query, metadata.getJavaType());
    }

    @Override
    public T partialSave(T entity, String... includeProperties) {
        return partialSave(
                entity,
                metadata.getId(entity),
                Arrays.asList(includeProperties)
        );
    }

    @Override
    public T partialSave(T entity, ID id, Collection<String> includeProperties) {
        var update = new HashMap<String, Object>();

        includeProperties.forEach(property -> {
            var value = resolveProperty(entity, property);
            update.put(property, value);
        });

        return partialSave(id, update);
    }

    @Override
    public T partialSave(ID id, String property, Object value) {
        var map = new HashMap<String, Object>();
        map.put(property, value);
        return partialSave(id, map);
    }

    @Override
    public T partialSave(ID id, Map<String, Object> updateValues) {
        var query = Query.query(where("id").is(id));
        var update = new Update();
        updateValues.entrySet().forEach(es -> {
            update.set(es.getKey(), es.getValue());
        });

        // return mongoOperations.updateFirst(query, update, entityClass);

        return mongoOperations.findAndModify(
                query,
                update,
                FindAndModifyOptions.options()
                        .returnNew(true),
                metadata.getJavaType()
        );
    }

    /***************************************************************************
     *                                                                         *
     * Private methods                                                         *
     *                                                                         *
     **************************************************************************/

    private Object resolveProperty(Object obj, String propertyName){

        if(obj == null) throw new ArgumentNullException("obj");
        if(propertyName == null) throw new ArgumentNullException("propertyName");
        if(propertyName.contains(".")) throw new IllegalArgumentException("propertyName must not be a path: " + propertyName);

        try {
            var fs = fields(obj.getClass());
            var field = fs.get(propertyName);
            return field.get(obj);
        }catch (Exception e){
            throw new IllegalStateException("Failed to access '" + propertyName + " on " + obj.getClass(), e);
        }
    }

    private Map<String, Field> fields(Class clazz){
        var fields = fieldIndex.get(clazz);
        if (fields == null) {
            fields = Arrays.stream(clazz.getDeclaredFields())
                    .collect(toMap(Field::getName, f -> f));

            fieldIndex.put(
                    clazz,
                    fields
            );
        }
        return fields;
    }

}
