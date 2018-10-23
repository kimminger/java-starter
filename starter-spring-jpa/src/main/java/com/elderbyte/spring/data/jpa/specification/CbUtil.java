package com.elderbyte.spring.data.jpa.specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Expression;
import java.util.Collection;

public class CbUtil {

    public static  <E> Expression<Boolean> isExpressionMember(CriteriaBuilder cb, Expression<E> a, Expression<?> b) {
        return cb.isMember(a, (Expression<? extends Collection<E>>) b);
    }

    public static  <E> Expression<Boolean> isElementMember(CriteriaBuilder cb, E elem, Expression<?> b) {
        return cb.isMember(elem, (Expression<? extends Collection<E>>) b);
    }
}
