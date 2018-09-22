package com.elderbyte.spring.data.jpa.integration.food;

import org.springframework.data.jpa.repository.JpaRepository;

public interface FoodMetadataRepository extends JpaRepository<FoodMetadata, String> {
}
