package com.elderbyte.spring.data.jpa.specification.builder;

import com.elderbyte.commons.exceptions.ArgumentNullException;
import com.elderbyte.spring.data.jpa.specification.sort.OrderSpecTemplate;

public class SortedSpecTemplate<T> {

    private final QueryParamSpecificationTemplate<T> filterTemplate;
    private final OrderSpecTemplate<T> orderTemplate;

    public SortedSpecTemplate(QueryParamSpecificationTemplate<T> filterTemplate, OrderSpecTemplate<T> orderTemplate) {

        if(filterTemplate == null) throw new ArgumentNullException("filterTemplate");
        if(orderTemplate == null) throw new ArgumentNullException("orderTemplate");

        this.filterTemplate = filterTemplate;
        this.orderTemplate = orderTemplate;
    }

    public QueryParamSpecificationTemplate<T> getFilterTemplate() {
        return filterTemplate;
    }

    public OrderSpecTemplate<T> getOrderTemplate() {
        return orderTemplate;
    }
}
