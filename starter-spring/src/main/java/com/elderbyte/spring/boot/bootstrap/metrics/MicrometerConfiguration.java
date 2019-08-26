package com.elderbyte.spring.boot.bootstrap.metrics;

import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.autoconfigure.metrics.MeterRegistryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MicrometerConfiguration {

    /**
     * Configure this variable in your Application - Properties File
     */
    @Value("${application.name}")
    private String applicationName;

    /**
     * If Set -
     * All Metrics will contain a common Tag with the name of the application
     * @param meterRegistry
     * @return
     */
    @Bean
    MeterRegistryCustomizer meterRegistryCustomizer(MeterRegistry meterRegistry) {
        return m -> meterRegistry.config().commonTags("application", applicationName);
    }
}
