package com.elderbyte.spring.data.jpa.specification.builder;

import com.elderbyte.commons.exceptions.ArgumentNullException;
import com.elderbyte.spring.data.jpa.specification.predicates.PredicateProviderPathValue;
import com.elderbyte.spring.data.jpa.specification.sort.OrderSpecTemplate;

public class SortedSpecTemplate<T> {

    private final QueryParamSpecificationTemplate<T> filterTemplate;
    private final OrderSpecTemplate<T> orderTemplate;
    private final PredicateProviderPathValue<T> defaultPredicateBuilder;


    public SortedSpecTemplate(
            QueryParamSpecificationTemplate<T> filterTemplate,
            OrderSpecTemplate<T> orderTemplate,
            PredicateProviderPathValue<T> defaultPredicateBuilder) {

        if(filterTemplate == null) throw new ArgumentNullException("filterTemplate");
        if(orderTemplate == null) throw new ArgumentNullException("orderTemplate");

        this.filterTemplate = filterTemplate;
        this.orderTemplate = orderTemplate;
        this.defaultPredicateBuilder = defaultPredicateBuilder;
    }

    public QueryParamSpecificationTemplate<T> getFilterTemplate() {
        return filterTemplate;
    }

    public OrderSpecTemplate<T> getOrderTemplate() {
        return orderTemplate;
    }




    /**
     * Builds the query param spec from this template
     */
    public QueryParamSpecBuilder<T> newSpec(){
        return new QueryParamSpecBuilder<>(
                filterTemplate,
                orderTemplate,
                defaultPredicateBuilder
        );
    }
}
