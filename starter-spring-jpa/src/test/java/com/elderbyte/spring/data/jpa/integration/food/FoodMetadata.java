package com.elderbyte.spring.data.jpa.integration.food;

import com.elderbyte.spring.data.jpa.integration.labels.Label;

import javax.persistence.*;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Entity
public class FoodMetadata {

    @Id
    private String id;


    @ManyToMany(fetch = FetchType.EAGER, cascade = { CascadeType.MERGE, CascadeType.REFRESH })
    private Set<Label> labels = new HashSet<>();

    public FoodMetadata(){}
    public FoodMetadata(String id, Label ...labels){
        this.id = id;
        this.labels.addAll(Arrays.asList(labels));

    }

    public Set<Label> getLabels() {
        return labels;
    }

    public String getId() {
        return id;
    }
}
