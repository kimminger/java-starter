package com.elderbyte.spring.data.jpa.specification.predicates.support;


import com.elderbyte.spring.data.jpa.specification.JpaPath;
import com.elderbyte.spring.data.jpa.specification.MatchablePredicateBuildStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.time.LocalDateTime;

public class DatePredicateBuildStrategy<T> implements MatchablePredicateBuildStrategy<T> {

    private final Logger log = LoggerFactory.getLogger(this.getClass());


    @Override
    public boolean canHandle(Root<T> root, String pathExpression, String value) {
        var path = JpaPath.resolve(root, pathExpression);
        return LocalDateTime.class.isAssignableFrom(path.getJavaType());
    }

    @Override
    public Predicate buildPredicate(Root<T> root, CriteriaBuilder cb, String pathExpression, String value) {

        var path = JpaPath.resolve(root, pathExpression);

        LocalDateTime date;

        try {
            date = LocalDateTime.parse(value); // TODO This wont handle format differences well!
        } catch (Exception e) {
            log.debug("Failed to parse date as LocalDateTime: " + value, e);
            date = LocalDateTime.MAX;
        }

        return cb.equal(path, date);
    }
}
