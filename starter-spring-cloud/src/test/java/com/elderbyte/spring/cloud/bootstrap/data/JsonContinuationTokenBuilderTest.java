package com.elderbyte.spring.cloud.bootstrap.data;

import com.elderbyte.commons.data.contiunation.JsonContinuationTokenBuilder;
import com.elderbyte.spring.cloud.bootstrap.integration.IntegrationTestApp;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = IntegrationTestApp.class)
public class JsonContinuationTokenBuilderTest {

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private JsonContinuationTokenBuilder continuationTokenBuilder;


    @Test
    public void ensureMapperFromSpringContext() {
        assertEquals(mapper, continuationTokenBuilder.getMapper());
    }
}