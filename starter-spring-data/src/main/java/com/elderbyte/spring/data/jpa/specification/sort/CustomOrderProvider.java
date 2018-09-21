package com.elderbyte.spring.data.jpa.specification.sort;

import org.springframework.data.domain.Sort;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Root;

@FunctionalInterface
public interface CustomOrderProvider<T> {

    javax.persistence.criteria.Order buildOrder(Sort.Order springOrder, Root<T> root, CriteriaBuilder cb);
}
