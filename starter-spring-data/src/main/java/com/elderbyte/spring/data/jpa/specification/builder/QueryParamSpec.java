package com.elderbyte.spring.data.jpa.specification.builder;

import com.elderbyte.spring.data.jpa.specification.predicates.PredicateExpressionSpecification;
import com.elderbyte.spring.data.jpa.specification.predicates.PredicateProvider;
import com.elderbyte.spring.data.jpa.specification.SpecificationPaged;
import com.elderbyte.spring.data.jpa.specification.SpecificationPagedImpl;
import com.elderbyte.spring.data.jpa.specification.expressions.Expression;
import com.elderbyte.spring.data.jpa.specification.sort.DynamicOrderBySpecification;
import com.elderbyte.spring.data.jpa.specification.sort.OrderSpecTemplate;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import java.util.Map;
import java.util.Set;

public class QueryParamSpec<T> {

    public static <T> QueryParamSpec<T> from(SortedSpecTemplate<T> specTemplate) {
        return new QueryParamSpec<>(specTemplate.getFilterTemplate(), specTemplate.getOrderTemplate());
    }

    /***************************************************************************
     *                                                                         *
     * Fields                                                                  *
     *                                                                         *
     **************************************************************************/

    private final QueryParamSpecificationTemplate<T> template;
    private final OrderSpecTemplate<T> orderSpecTemplate;

    /***************************************************************************
     *                                                                         *
     * Constructor                                                             *
     *                                                                         *
     **************************************************************************/

    public QueryParamSpec(QueryParamSpecificationTemplate<T> template, OrderSpecTemplate<T> orderSpecTemplate){
        this.template = template;
        this.orderSpecTemplate = orderSpecTemplate;
    }

    /***************************************************************************
     *                                                                         *
     * Public API                                                              *
     *                                                                         *
     **************************************************************************/

    public SpecificationPaged<T> build(Map<String, Set<String>> queryParams, Pageable pageable){

        Specification<T> sortedSpec = build(queryParams, pageable.getSort());

        return new SpecificationPagedImpl<>(
                sortedSpec,
                PageRequest.of(pageable.getPageNumber(), pageable.getPageSize())
        );
    }

    public Specification<T> build(Map<String, Set<String>> queryParams, Sort sort){
        Specification<T> spec = build(queryParams);
        return new DynamicOrderBySpecification<>(spec, orderSpecTemplate, sort);
    }

    public Specification<T> build(Map<String, Set<String>> queryParams){
        Expression<PredicateProvider<T>> expression = template.resolve(queryParams);
        return new PredicateExpressionSpecification<>(expression);
    }

}
