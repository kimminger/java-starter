package com.elderbyte.spring.data.jpa.integration;

import com.elderbyte.spring.data.jpa.integration.food.Food;
import com.elderbyte.spring.data.jpa.integration.food.FoodRepository;
import com.elderbyte.spring.data.jpa.specification.builder.QueryParamsBuilder;
import com.elderbyte.spring.data.jpa.specification.builder.SpecTemplateBuilder;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = DataIntegrationTestApp.class)
public class SpecificationTest {


    @Autowired
    private FoodRepository repository;

    @Before
    @Transactional
    public void prepare(){

        repository.save(new Food("apple", 10, "Hello"));
        repository.save(new Food("lattice", 15, "Hello"));
        repository.save(new Food("bread", 1, "Hello 15 now"));
        repository.save(new Food("plum", 6, "Hello"));
        repository.save(new Food("bean", 33, "Abcd"));


    }

    @Test
    public void contextLoads(){ }

    @Test
    public void specificationTest_Simple(){

        var specTemplate = SpecTemplateBuilder.start(Food.class)
                            .paramPath("name", "name")
                            .paramPath("age", "age")
                            .build();


        var queryParams = QueryParamsBuilder.start()
                                        .add("age", "15")
                                        .build();


        var spec = specTemplate.newSpec()
                    .distinct()
                    .build(queryParams);

        // Test

        var found = repository.findAll(spec);

        Assert.assertEquals(1, found.size());
        Assert.assertEquals("lattice", found.get(0).getName());
    }

    @Test
    public void specificationTest_OrField(){

        var specTemplate = SpecTemplateBuilder.start(Food.class)
                .paramPath("name", "name")
                .paramPath("age", "age")
                .paramPathAny("multi", "age", "name", "description")
                .build();


        var queryParams = QueryParamsBuilder.start()
                .add("multi", "15")
                .build();

        var spec = specTemplate.newSpec()
                        .build(queryParams);

        // Test

        var found = repository.findAll(spec);

        Assert.assertEquals(2, found.size());
    }

    @Test
    public void specificationTest_Custom(){

        var specTemplate = SpecTemplateBuilder.start(Food.class)
                .paramPathCustom("a", "age", (root, cb, path, value) -> cb.or(
                        cb.equal(path, 10),
                        cb.equal(path, Integer.parseInt(value)) // Expect 15 from the dynamic query params
                ))
                .build();


        var queryParams = QueryParamsBuilder.start()
                .add("a", "15")
                .build();

        var spec = specTemplate.newSpec()
                        .build(queryParams);

        // Test

        var found = repository.findAll(spec);

        Assert.assertEquals(2, found.size());
    }

    @Test
    public void specificationTest_Custom_Static(){

        var specTemplate = SpecTemplateBuilder.start(Food.class)
                .build();

        var spec = specTemplate.newSpec()
                .and("age", (r,cb,path) -> cb.or(
                        cb.equal(path, 10),
                        cb.equal(path, 15)
                ))
                .build();

        // Test

        var found = repository.findAll(spec);

        Assert.assertEquals(2, found.size());
    }


    @Test
    public void specificationTest_Static(){

        var specTemplate = SpecTemplateBuilder.start(Food.class)
                .paramPathAny("multi", "age", "name", "description")
                .build();

        var queryParams = QueryParamsBuilder.start()
                .add("multi", "15")
                .build();

        var spec = specTemplate.newSpec()
                .andEquals("age", 15)
                .build(queryParams);

        // Test

        var found = repository.findAll(spec);

        Assert.assertEquals(1, found.size());
    }


    @Test
    public void specificationTest_Number_Expression(){

        var specTemplate = SpecTemplateBuilder.start(Food.class)
                .paramPath("age", "age")
                .build();


        var queryParams = QueryParamsBuilder.start()
                .add("age", ">=10")
                .add("age", "<30")
                .build();


        var spec = specTemplate.newSpec()
                .distinct()
                .build(queryParams);

        // Test

        var found = repository.findAll(spec);

        Assert.assertEquals(2, found.size());
    }
}
