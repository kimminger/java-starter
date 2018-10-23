package com.elderbyte.spring.data.jpa.specification;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

public interface SpecificationPaged<T> extends Specification<T> {

    Pageable getPageable();

}
