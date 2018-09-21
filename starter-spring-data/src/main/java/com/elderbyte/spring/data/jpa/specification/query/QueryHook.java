package com.elderbyte.spring.data.jpa.specification.query;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

public interface QueryHook<T> {

    void handle(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder cb);
}
