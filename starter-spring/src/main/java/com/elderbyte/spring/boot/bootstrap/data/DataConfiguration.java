package com.elderbyte.spring.boot.bootstrap.data;

import com.elderbyte.commons.data.contiunation.JsonContinuationToken;
import com.elderbyte.commons.data.contiunation.JsonContinuationTokenBuilder;
import com.elderbyte.spring.boot.bootstrap.data.pagination.PageJacksonModule;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataConfiguration {

    @Bean
    public Module pageJacksonModule(){
        return new PageJacksonModule();
    }

    @Bean
    // @ConditionalOnBean(ObjectMapper.class) // TODO Dangerous -> will disable this bean when object mapper is later constructed
    public JsonContinuationTokenBuilder JsonContinuationTokenBuilder(ObjectMapper mapper){
        var builder = new JsonContinuationTokenBuilder(mapper);
        JsonContinuationToken.setBuilder(builder);
        return builder;
    }
}
