package com.elderbyte.spring.data.mongo.repositories;

import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.mapping.BasicMongoPersistentEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.support.MappingMongoEntityInformation;
import org.springframework.data.mongodb.repository.support.MongoRepositoryFactory;
import org.springframework.data.mongodb.repository.support.MongoRepositoryFactoryBean;
import org.springframework.data.repository.core.RepositoryMetadata;
import org.springframework.data.repository.core.support.RepositoryFactorySupport;
import org.springframework.data.util.ClassTypeInformation;
import org.springframework.data.util.TypeInformation;

import java.io.Serializable;

public class ElderMongoRepositoryFactoryBean<R extends MongoRepository<T, I>, T, I extends Serializable>
        extends MongoRepositoryFactoryBean<R, T, I> {

    /**
     * Creates a new {@link MongoRepositoryFactoryBean} for the given repository interface.
     *
     * @param repositoryInterface must not be {@literal null}.
     */
    public ElderMongoRepositoryFactoryBean(Class<? extends R> repositoryInterface) {
        super(repositoryInterface);
    }

    @Override
    protected RepositoryFactorySupport getFactoryInstance(MongoOperations operations) {
        return new ElderMongoRepositoryFactory<T,I>(operations);
    }

    private static class ElderMongoRepositoryFactory<T, ID extends Serializable> extends MongoRepositoryFactory {

        private MongoOperations mongo;
        public ElderMongoRepositoryFactory(MongoOperations mongoOperations) {
            super(mongoOperations);
            this.mongo = mongoOperations;
        }

        @SuppressWarnings("unchecked")
        protected Object getTargetRepository(RepositoryMetadata metadata) {

            TypeInformation<T> information =  ClassTypeInformation.from((Class<T>)metadata.getDomainType());
            var pe = new BasicMongoPersistentEntity<T>(information);
            var mongometa = new MappingMongoEntityInformation<T, ID>(pe);

            return new EntityMongoRepositoryImpl<>(mongometa, (MongoTemplate) mongo);
        }

        protected Class<?> getRepositoryBaseClass(RepositoryMetadata metadata) {
            return EntityMongoRepository.class;
        }
    }
}
