package com.elderbyte.spring.data.jpa.integration.food;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface FoodRepository extends JpaRepository<Food, String>, JpaSpecificationExecutor<Food> {

}
