package com.elderbyte.spring.cloud.bootstrap.integration;

import com.elderbyte.commons.data.contiunation.ContinuableListing;
import com.elderbyte.commons.data.contiunation.ContinuableListingImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.Arrays;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = IntegrationTestApp.class)
public class ContinuableListingJacksonTests {

    @Autowired
    private ObjectMapper mapper;

    @Test
    public void test() throws IOException {

        var listing = ContinuableListing.finiteChunk(Arrays.asList("foo", "bar"));
        String json = mapper.writeValueAsString(listing);


        var listingAgain = mapper.readValue(json, ContinuableListing.class);

        Assert.assertTrue(listingAgain instanceof ContinuableListingImpl);
        Assert.assertEquals("foo", listing.getContent().get(0));
    }
}
