package com.elderbyte.spring.data.jpa.specification;

import com.elderbyte.spring.data.jpa.specification.predicates.PredicateBuildStrategy;

import javax.persistence.criteria.Root;

/**
 * A strategy which is able to newSpec a JPA predicate
 */
public interface MatchablePredicateBuildStrategy<T> extends PredicateBuildStrategy<T> {

    /**
     * Returns true if this strategy is able to handle the given path / type.
     */
    boolean canHandle(Root<T> root, String pathExpression, String value);

}
