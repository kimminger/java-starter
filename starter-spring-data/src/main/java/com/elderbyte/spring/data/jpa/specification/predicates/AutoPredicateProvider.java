package com.elderbyte.spring.data.jpa.specification.predicates;

import com.elderbyte.spring.data.jpa.specification.MatchablePredicateBuildStrategy;
import com.elderbyte.spring.data.jpa.specification.predicates.support.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static java.util.stream.Collectors.toList;

public class AutoPredicateProvider<T> implements PredicateProviderPathValue<T> {

    /***************************************************************************
     *                                                                         *
     * Fields                                                                  *
     *                                                                         *
     **************************************************************************/

    private static final Logger logger = LoggerFactory.getLogger(AutoPredicateProvider.class);

    private final List<MatchablePredicateBuildStrategy<T>> predicateBuildStrategies = new ArrayList<>();

    /***************************************************************************
     *                                                                         *
     * Constructor                                                             *
     *                                                                         *
     **************************************************************************/

    public AutoPredicateProvider(){
        this(
                Arrays.asList(
                        new NullPredicateBuildStrategy<>(),
                        new StringPredicateBuildStrategy<>(),
                        new BooleanPredicateBuildStrategy<>(),
                        new DatePredicateBuildStrategy<>(),
                        new EnumPredicateBuildStrategy<>(),
                        new IntegerPredicateBuildStrategy<>(),
                        new DoublePredicateBuildStrategy<>(),
                        new BigDecimalPredicateBuildStrategy<>()
                )
        );
    }

    public AutoPredicateProvider(Collection<MatchablePredicateBuildStrategy<T>> predicateBuildStrategies){
        this.predicateBuildStrategies.addAll(predicateBuildStrategies);
    }

    /***************************************************************************
     *                                                                         *
     * Public API                                                              *
     *                                                                         *
     **************************************************************************/

    @Override
    public Predicate buildPredicate(Root<T> root, CriteriaBuilder cb, String pathExpression, String value) {
        var matching = findMatching(root, pathExpression, value);
        return matching.buildPredicate(root, cb, pathExpression, value);
    }

    @Override
    public String toString() {
        return "AutoPredicateProvider{" +
                ", predicateBuildStrategies=" + predicateBuildStrategies.stream().map(s -> s.getClass().getSimpleName()).collect(toList()) +
                '}';
    }

    /***************************************************************************
     *                                                                         *
     * Private methods                                                         *
     *                                                                         *
     **************************************************************************/

    private PredicateProviderPathValue<T> findMatching(Root<T> root, String pathExpression, String value) {
        return predicateBuildStrategies.stream()
                .filter(s -> s.canHandle(root, pathExpression, value))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Not supported entity property type: " + pathExpression));
    }

}
