package com.elderbyte.spring.data.jpa.specification.sort;

import java.util.HashMap;
import java.util.Map;

public class OrderSpecTemplate<T> {
    private final Map<String, CustomOrderProvider<T>> customOrderHandlers = new HashMap<>();

    public OrderSpecTemplate(Map<String, CustomOrderProvider<T>> customOrderHandlers){
        this.customOrderHandlers.putAll(customOrderHandlers);
    }


    public CustomOrderProvider<T> getCustomOrder(String sortKey){
        return this.customOrderHandlers.get(sortKey);
    }

}
