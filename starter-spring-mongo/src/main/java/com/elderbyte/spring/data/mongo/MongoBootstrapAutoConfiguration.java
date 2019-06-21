package com.elderbyte.spring.data.mongo;


import com.elderbyte.spring.data.mongo.config.DefaultMongoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({
        DefaultMongoConfiguration.class
})
public class MongoBootstrapAutoConfiguration {

}
