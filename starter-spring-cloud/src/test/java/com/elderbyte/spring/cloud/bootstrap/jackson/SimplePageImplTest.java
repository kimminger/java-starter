package com.elderbyte.spring.cloud.bootstrap.jackson;

import org.junit.Assert;
import org.junit.Test;

import com.elderbyte.spring.cloud.bootstrap.jackson.PageJacksonModule.SimplePageImpl;

import java.util.Arrays;


public class SimplePageImplTest {



    @Test
    public void hasNext() throws Exception {

        PageJacksonModule.SimplePageImpl<Object> page = new SimplePageImpl<>(
                Arrays.asList(new Object(), new Object()),
                1,
                30,
                90
                );

        Assert.assertEquals(true, page.hasNext());
    }

    @Test
    public void hasPrevious() throws Exception {
        SimplePageImpl<Object> page = new SimplePageImpl<>(
                Arrays.asList(new Object(), new Object()),
                1,
                30,
                90
        );

        Assert.assertEquals(true, page.hasPrevious());
    }

    @Test
    public void nextPageable() throws Exception {
        SimplePageImpl<Object> page = new SimplePageImpl<>(
                Arrays.asList(new Object(), new Object()),
                1,
                30,
                90
        );
        Assert.assertEquals(2 , page.nextPageable().getPageNumber());
    }

    @Test
    public void previousPageable() throws Exception {
        SimplePageImpl<Object> page = new SimplePageImpl<>(
                Arrays.asList(new Object(), new Object()),
                1,
                30,
                90
        );
        Assert.assertEquals(0 , page.previousPageable().getPageNumber());
    }

}
