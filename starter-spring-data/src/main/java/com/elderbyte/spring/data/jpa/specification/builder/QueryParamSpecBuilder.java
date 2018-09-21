package com.elderbyte.spring.data.jpa.specification.builder;

import com.elderbyte.spring.data.jpa.specification.predicates.PredicateExpressionSpecification;
import com.elderbyte.spring.data.jpa.specification.predicates.PredicateProvider;
import com.elderbyte.spring.data.jpa.specification.SpecificationPaged;
import com.elderbyte.spring.data.jpa.specification.SpecificationPagedImpl;
import com.elderbyte.spring.data.jpa.specification.expressions.Expression;
import com.elderbyte.spring.data.jpa.specification.query.QueryHook;
import com.elderbyte.spring.data.jpa.specification.sort.DynamicOrderBySpecification;
import com.elderbyte.spring.data.jpa.specification.sort.OrderSpecTemplate;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class QueryParamSpecBuilder<T> {

    public static <T> QueryParamSpecBuilder<T> from(SortedSpecTemplate<T> specTemplate) {
        return new QueryParamSpecBuilder<>(specTemplate.getFilterTemplate(), specTemplate.getOrderTemplate());
    }

    /***************************************************************************
     *                                                                         *
     * Fields                                                                  *
     *                                                                         *
     **************************************************************************/

    private final QueryParamSpecificationTemplate<T> template;
    private final OrderSpecTemplate<T> orderSpecTemplate;

    private final List<QueryHook<T>> hooks = new ArrayList<>();

    /***************************************************************************
     *                                                                         *
     * Constructor                                                             *
     *                                                                         *
     **************************************************************************/

    public QueryParamSpecBuilder(QueryParamSpecificationTemplate<T> template, OrderSpecTemplate<T> orderSpecTemplate){
        this.template = template;
        this.orderSpecTemplate = orderSpecTemplate;
    }

    /***************************************************************************
     *                                                                         *
     * Public API                                                              *
     *                                                                         *
     **************************************************************************/

    public QueryParamSpecBuilder<T> distinct() {
        configureQuery((root, qry, cb) -> qry.distinct(true));
        return this;
    }

    public QueryParamSpecBuilder<T> configureQuery(QueryHook<T> queryHook){
        hooks.add(queryHook);
        return this;
    }

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
        return finalize(new PredicateExpressionSpecification<>(expression));
    }

    /***************************************************************************
     *                                                                         *
     * Private methods                                                         *
     *                                                                         *
     **************************************************************************/

    private Specification<T> finalize(Specification<T> specification){
        return new SpecificationHook<>(specification, hooks);
    }

}
