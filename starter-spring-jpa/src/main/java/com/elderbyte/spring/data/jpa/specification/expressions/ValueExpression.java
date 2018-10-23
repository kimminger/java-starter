package com.elderbyte.spring.data.jpa.specification.expressions;

public class ValueExpression<T> implements Expression<T> {

    private final T value;

    public ValueExpression(T value){
        this.value = value;
    }

    public T getValue() {
        return value;
    }

    @Override
    public String toString() {
        return value + "";
    }
}
