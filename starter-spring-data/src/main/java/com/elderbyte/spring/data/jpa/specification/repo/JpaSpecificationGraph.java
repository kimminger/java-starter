package com.elderbyte.spring.data.jpa.specification.repo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.List;

@NoRepositoryBean
public interface JpaSpecificationGraph<T> extends JpaSpecificationExecutor<T> {

    List<T> findAll(Specification<T> spec, EntityGraph.EntityGraphType entityGraphType, String entityGraphName);
    Page<T> findAll(Specification<T> spec, Pageable pageable, EntityGraph.EntityGraphType entityGraphType, String entityGraphName);
    List<T> findAll(Specification<T> spec, Sort sort, EntityGraph.EntityGraphType entityGraphType, String entityGraphName);
    T findOne(Specification<T> spec, EntityGraph.EntityGraphType entityGraphType, String entityGraphName);

}
