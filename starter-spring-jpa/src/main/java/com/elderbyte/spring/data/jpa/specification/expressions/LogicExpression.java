package com.elderbyte.spring.data.jpa.specification.expressions;


import java.util.List;

public class LogicExpression<T> extends BinrayExpression<T> {

    /***************************************************************************
     *                                                                         *
     * Static Builder                                                          *
     *                                                                         *
     **************************************************************************/

    public static <T> Expression<T> conjunction(List<Expression<T>> conjunction){

        if(conjunction.isEmpty()){
            return null;
        }else if(conjunction.size() == 1){
            return conjunction.get(0);
        }else if(conjunction.size() == 2){
            return and(conjunction.get(0), conjunction.get(1));
        }else{
            // More than 2
            var expr = conjunction.get(0);
            boolean first = true;

            for(var next: conjunction){
                if(first) {
                    first = false;
                    continue;
                }
                expr = and(expr, next);
            }
            return expr;
        }
    }

    public static <T> Expression<T> disjunction(List<? extends Expression<T>> disjunction){

        if(disjunction.isEmpty()){
            return null;
        }else if(disjunction.size() == 1){
            return disjunction.get(0);
        }else if(disjunction.size() == 2){
            return or(disjunction.get(0), disjunction.get(1));
        }else{
            // More than 2
            var expr = disjunction.get(0);
            boolean first = true;

            for(var next: disjunction){
                if(first) {
                    first = false;
                    continue;
                }
                expr = or(expr, next);
            }
            return expr;
        }
    }

    public static  <T> Expression<T> or(Expression<T> a, Expression<T> b){
        if(a == null){
            return b;
        }else if(b == null){
            return a;
        }else{
            return new LogicExpression<>(a, LogicOperator.OR, b);
        }
    }

    public static  <T> Expression<T> and(Expression<T> a, Expression<T> b){
        if(a == null){
            return b;
        }else if(b == null){
            return a;
        }else{
            return new LogicExpression<>(a, LogicOperator.AND, b);
        }
    }


    /***************************************************************************
     *                                                                         *
     * Inner Enum                                                              *
     *                                                                         *
     **************************************************************************/

    public enum  LogicOperator {
        OR,
        AND
    }


    /***************************************************************************
     *                                                                         *
     * Fields                                                                  *
     *                                                                         *
     **************************************************************************/

    private final LogicOperator operator;


    /***************************************************************************
     *                                                                         *
     * Constructor                                                             *
     *                                                                         *
     **************************************************************************/

    public LogicExpression(Expression<T> left, LogicOperator operator, Expression<T> right) {
        super(left, right);
        this.operator = operator;
    }

    public LogicOperator getOperator() {
        return operator;
    }

    @Override
    public String toString() {
        return "(" + getLeft() + " " + getOperator() + " " + getRight() + ")";
    }
}
