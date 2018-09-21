package com.elderbyte.spring.data.jpa.integration.food;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Objects;

@Entity
public class Food {

    @Id
    private String name;

    private int age;

    private String description;

    public Food(){}
    public Food(String name, int age, String description){
        this.name = name;
        this.age = age;
        this.description = description;
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
