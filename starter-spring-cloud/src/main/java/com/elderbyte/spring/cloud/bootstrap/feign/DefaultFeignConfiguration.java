package com.elderbyte.spring.cloud.bootstrap.feign;


import com.elderbyte.spring.boot.bootstrap.data.pagination.PageJacksonModule;
import com.fasterxml.jackson.databind.Module;
import feign.codec.Decoder;
import feign.codec.Encoder;
import feign.codec.ErrorDecoder;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.openfeign.FeignFormatterRegistrar;
import org.springframework.cloud.openfeign.support.ResponseEntityDecoder;
import org.springframework.cloud.openfeign.support.SpringDecoder;
import org.springframework.cloud.openfeign.support.SpringEncoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.datetime.standard.DateTimeFormatterRegistrar;

/**
 * Default feign integration
 * This class would actually belong into a feign-spring-advanced library, but for now it is here.
 * Feign should get out of the box support for most of these things anyway.
 */
@Configuration
public class DefaultFeignConfiguration {

    @Autowired
    private ObjectFactory<HttpMessageConverters> messageConverters;

    @Bean
    public Encoder feignEncoder() {
        return new ContinuationTokenQueryEncoder(new PageableQueryEncoder(new SpringEncoder(messageConverters)));
    }

    @Bean
    public Decoder feignDecoder() {
        return new OptionalFrom404Decoder(new ResponseEntityDecoder(new SpringDecoder(messageConverters)));
    }

    @Bean
    public ErrorDecoder feignErrorDecoder(){
        return new SpringWebClientErrorDecoder();
    }

    @Bean
    public FeignBuilderService feignBuilderService() { return new FeignBuilderService(); }

    /**
     * Bugfix for Feign not knowing how to handle @DateTimeFormat in query parameters
     *
     * see https://github.com/spring-cloud/spring-cloud-netflix/issues/1178
     */
    @Bean
    public FeignFormatterRegistrar localDateFeignFormatterRegistrar() {
        return formatterRegistry -> {
            DateTimeFormatterRegistrar registrar = new DateTimeFormatterRegistrar();
            registrar.setUseIsoFormat(true);
            registrar.registerFormatters(formatterRegistry);
        };
    }
}
