package com.elderbyte.spring.data.jpa.specification.expressions;

public class BinrayExpression<T> implements Expression<T> {

    private final Expression<T> left;
    private final Expression<T> right;

    public BinrayExpression(Expression<T> left, Expression<T> right) {
        this.left = left;
        this.right = right;
    }

    public Expression<T> getLeft() {
        return left;
    }

    public Expression<T> getRight() {
        return right;
    }
}
