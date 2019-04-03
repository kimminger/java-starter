package com.elderbyte.spring.bootstrap.servlet.test;

import com.elderbyte.spring.boot.bootstrap.data.pagination.PageJacksonModule;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit4.SpringRunner;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = IntegrationTestApp.class)
public class AppContextTests {


    @Autowired
    private ApplicationContext applicationContext;

    @Test
    public void contextLoads(){
        Assert.assertTrue(applicationContext != null);
    }


    @Test // expected = NoSuchBeanDefinitionException.class
    public void ensurePageJacksonModuleIsPresent(){
        applicationContext.getBean(PageJacksonModule.class);
    }

}
