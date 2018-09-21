package com.elderbyte.spring.data.jpa.specification;

import com.elderbyte.spring.data.jpa.specification.predicates.PredicateBuildStrategy;

import javax.persistence.criteria.Root;

/**
 * A strategy which is able to build a JPA predicate
 */
public interface MatchablePredicateBuildStrategy extends PredicateBuildStrategy {

    /**
     * Returns true if this strategy is able to handle the given path / type.
     */
    boolean canHandle(Root<?> root, String pathExpression, String value);

}
