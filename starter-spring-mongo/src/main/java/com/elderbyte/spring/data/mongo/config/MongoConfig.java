package com.elderbyte.spring.data.mongo.config;

import org.springframework.context.annotation.Configuration;

@Configuration
public class MongoConfig {

    /**
     * Defines the top level package from where Spring will recursively search for
     * Mongo repositories and initialize them.
     */
    public static final String MONGO_BASE_PACKAGES = "${elder.mongo.base-packages:com.elderbyte}";

}
