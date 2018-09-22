package com.elderbyte.spring.data.jpa.integration.food;

import com.elderbyte.spring.data.jpa.integration.labels.Label;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class Food {

    @Id
    private String name;

    private int age;

    private String description;

    @ManyToOne(fetch = FetchType.EAGER, optional = true)
    private FoodMetadata metadata = null;

    public Food(){}

    public Food(String name, int age, String description){
        this(name, age, description, null);
    }
    public Food(String name, int age, String description, FoodMetadata metadata){
        this.name = name;
        this.age = age;
        this.description = description;
        this.metadata = metadata;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Food food = (Food) o;
        return Objects.equals(name, food.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }



}
