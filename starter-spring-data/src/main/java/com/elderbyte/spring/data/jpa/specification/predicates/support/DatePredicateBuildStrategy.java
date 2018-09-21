package com.elderbyte.spring.data.jpa.specification.predicates.support;


import com.elderbyte.spring.data.jpa.specification.JpaPathExpression;
import com.elderbyte.spring.data.jpa.specification.MatchablePredicateBuildStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.time.LocalDateTime;

public class DatePredicateBuildStrategy implements MatchablePredicateBuildStrategy {

    private final Logger log = LoggerFactory.getLogger(this.getClass());


    @Override
    public boolean canHandle(Root<?> root, String pathExpression, String value) {
        var path = JpaPathExpression.resolve(root, pathExpression);
        return LocalDateTime.class.isAssignableFrom(path.getJavaType());
    }

    @Override
    public Predicate buildPredicate(Root<?> root, String pathExpression, CriteriaBuilder criteriaBuilder, String value) {

        var path = JpaPathExpression.resolve(root, pathExpression);

        LocalDateTime date;

        try {
            date = LocalDateTime.parse(value);
        } catch (Exception e) {
            log.debug("Failed to parse date as LocalDateTime: " + value, e);
            date = LocalDateTime.MAX;
        }

        return criteriaBuilder.equal(path, date);
    }
}
