package com.elderbyte.spring.data.jpa.specification.sort;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.query.QueryUtils;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DynamicOrderBySpecification<T> implements Specification<T> {

    private final Specification<T> specification;
    private final Sort sort;
    private final OrderSpecTemplate<T> customOrderHandlers;

    public DynamicOrderBySpecification(Specification<T> specification, OrderSpecTemplate<T> customOrderHandlers, Sort sort){
        this.specification = specification;
        this.customOrderHandlers = customOrderHandlers;
        this.sort = sort;
    }


    @Override
    public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        var predicate = this.specification.toPredicate(root, query, cb);
        query.orderBy(buildOrders(root, cb));
        return predicate;
    }

    private List<Order> buildOrders(Root<T> root, CriteriaBuilder cb){
        var orders = new ArrayList<Order>();

        for(var order : sort){

            javax.persistence.criteria.Order jpaOrder;
            // if special handling, custom order
            var customHandler = customOrderHandlers.getCustomOrder(order.getProperty());

            if(customHandler != null){
                jpaOrder = customHandler.buildOrder(order, root, cb);
            }else{
                jpaOrder = buildJpaOrder(order, root, cb);
            }
            // otherwise default
            orders.add(jpaOrder);
        }

        return orders;
    }

    private javax.persistence.criteria.Order buildJpaOrder(Sort.Order springOrder, Root<T> root, CriteriaBuilder cb){
        var orders = QueryUtils.toOrders(Sort.by(springOrder), root, cb);
        return orders.get(0);
    }
}
