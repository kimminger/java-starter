package com.elderbyte.spring.data.mongo.config;

import com.elderbyte.spring.data.mongo.converters.MongoJsonNode;
import com.elderbyte.spring.data.mongo.converters.MongoOffsetDateTimeUtc;
import com.elderbyte.spring.data.mongo.repositories.EntityMongoRepositoryImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import java.util.ArrayList;


@Configuration
@EnableMongoRepositories(
        basePackages = MongoConfig.MONGO_BASE_PACKAGES,
        repositoryBaseClass = EntityMongoRepositoryImpl.class
)
public class DefaultMongoConfiguration {


    @Bean
    public MongoCustomConversions customConversions(ObjectMapper mapper){
        var converters = new ArrayList<Converter<?,?>>();
        converters.add(new MongoOffsetDateTimeUtc.OffsetDateTimeToUtcConverter());
        converters.add(new MongoOffsetDateTimeUtc.UtcToOffsetDateTimeConverter());

        converters.add(new MongoJsonNode.DocumentToJsonNodeConverter(mapper));
        converters.add(new MongoJsonNode.JsonNodeToDocumentConverter(mapper));

        return new MongoCustomConversions(converters);
    }

}

