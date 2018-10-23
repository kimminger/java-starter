package com.elderbyte.spring.data.jpa.specification.builder;

import com.elderbyte.spring.data.jpa.specification.query.QueryHook;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class SpecificationHook<T> implements Specification<T> {

    private final Specification<T> specification;
    private final List<QueryHook<T>> hooks = new ArrayList<>();

    public SpecificationHook(Specification<T> specification, Collection<QueryHook<T>> hooks){
        this.specification = specification;
        this.hooks.addAll(hooks);
    }

    @Override
    public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        var predicate = specification.toPredicate(root, query, cb);
        hooks.forEach(h -> h.handle(root, query, cb));
        return predicate;
    }


    @Override
    public String toString() {
        return specification.toString();
    }
}
