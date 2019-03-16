package com.elderbyte.spring.boot.bootstrap.jackson;

import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

import static com.fasterxml.jackson.databind.DeserializationFeature.*;
import static com.fasterxml.jackson.databind.SerializationFeature.FAIL_ON_EMPTY_BEANS;
import static com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATES_AS_TIMESTAMPS;

@Configuration
public class DefaultJacksonConfiguration {

    @Bean
    public Jackson2ObjectMapperBuilderCustomizer jackson2ObjectMapperBuilder() {

        return builder -> {
            TimeZone tz = TimeZone.getTimeZone("UTC");
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm'Z'"); // ISO Date/Time Format
            df.setTimeZone(tz);

            builder
                    .featuresToEnable(

                            // Enabled (Serialization)
                            FAIL_ON_EMPTY_BEANS,

                            // Enabled (Deserialization)
                            FAIL_ON_INVALID_SUBTYPE,
                            FAIL_ON_NUMBERS_FOR_ENUMS,
                            FAIL_ON_NULL_FOR_PRIMITIVES,
                            FAIL_ON_READING_DUP_TREE_KEY
                    )

                    .featuresToDisable(

                            // Disabled (Serialization)
                            WRITE_DATES_AS_TIMESTAMPS,

                            // Disabled (Deserialization)
                            FAIL_ON_UNKNOWN_PROPERTIES,
                            FAIL_ON_IGNORED_PROPERTIES
                    )
                    .dateFormat(df);

        };
    }

}
