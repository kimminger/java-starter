package com.elderbyte.spring.data.jpa.specification;

import com.elderbyte.commons.exceptions.ArgumentNullException;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public class SpecificationPagedImpl<T> implements SpecificationPaged<T> {

    /***************************************************************************
     *                                                                         *
     * Fields                                                                  *
     *                                                                         *
     **************************************************************************/

    private final Pageable pageable;
    private final Specification<T> specification;

    /***************************************************************************
     *                                                                         *
     * Constructor                                                             *
     *                                                                         *
     **************************************************************************/

    public SpecificationPagedImpl(Specification<T> specification, Pageable pageable){

        if(specification == null) throw new ArgumentNullException("specification");
        if(pageable == null) throw new ArgumentNullException("pageable");

        this.specification = specification;
        this.pageable = pageable;
    }

    /***************************************************************************
     *                                                                         *
     * Public API                                                              *
     *                                                                         *
     **************************************************************************/

    @Override
    public Pageable getPageable() {
        return pageable;
    }


    @Override
    public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        return specification.toPredicate(root, query, cb);
    }
}
