package com.elderbyte.spring.data.jpa.specification.predicates;

import com.elderbyte.spring.data.jpa.specification.JpaPathExpression;
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

public class AutoPredicateBuildStrategy implements PredicateBuildStrategy {

    /***************************************************************************
     *                                                                         *
     * Fields                                                                  *
     *                                                                         *
     **************************************************************************/

    private static final Logger logger = LoggerFactory.getLogger(AutoPredicateBuildStrategy.class);

    private static final List<MatchablePredicateBuildStrategy> DEFAULT_STRATEGIES = Arrays.asList(
            new NullPredicateBuildStrategy(),
            new StringPredicateBuildStrategy(),
            new BooleanPredicateBuildStrategy(),
            new DatePredicateBuildStrategy(),
            new EnumPredicateBuildStrategy(),
            new IntegerPredicateBuildStrategy(),
            new DoublePredicateBuildStrategy(),
            new BigDecimalPredicateBuildStrategy()
    );

    private final List<MatchablePredicateBuildStrategy> predicateBuildStrategies = new ArrayList<>();

    /***************************************************************************
     *                                                                         *
     * Constructor                                                             *
     *                                                                         *
     **************************************************************************/

    public AutoPredicateBuildStrategy(){
        this(DEFAULT_STRATEGIES);
    }

    public AutoPredicateBuildStrategy(Collection<MatchablePredicateBuildStrategy> predicateBuildStrategies){
        this.predicateBuildStrategies.addAll(predicateBuildStrategies);
    }

    /***************************************************************************
     *                                                                         *
     * Public API                                                              *
     *                                                                         *
     **************************************************************************/

    @Override
    public Predicate buildPredicate(Root<?> root, String pathExpression, CriteriaBuilder cb, String value) {
        return buildDisjunction(root, cb, pathExpression, value);
        //var matching = findMatching(root, pathExpression, value);
        //return matching.buildPredicate(root, pathExpression, cb, value);
    }

    @Override
    public String toString() {
        return "AutoPredicateBuildStrategy{" +
                ", predicateBuildStrategies=" + predicateBuildStrategies.stream().map(s -> s.getClass().getSimpleName()).collect(toList()) +
                '}';
    }

    /***************************************************************************
     *                                                                         *
     * Private methods                                                         *
     *                                                                         *
     **************************************************************************/

    private Predicate buildDisjunction(Root<?> root, CriteriaBuilder cb, String pathExpression, String value){
        if (JpaPathExpression.isDisjunction(pathExpression)) {

            var disjunction = new ArrayList<Predicate>();
            for(String part : JpaPathExpression.parseDisjunction(pathExpression)){
                try {
                    disjunction.add(findAndBuildPredicate(root, cb, part, value));
                }catch (IllegalArgumentException e){
                    logger.warn("Ignoring disjunction part '" + part + "' due to not matching types! " + e.toString());
                }
            }
            return cb.or(disjunction.toArray(new Predicate[0]));
        }else{
            return findAndBuildPredicate(root, cb, pathExpression, value);
        }
    }

    private Predicate findAndBuildPredicate(Root<?> root, CriteriaBuilder cb, String pathExpression, String value) {
        var matching = findMatching(root, pathExpression, value);
        return matching.buildPredicate(root, pathExpression, cb, value);
    }

    private PredicateBuildStrategy findMatching(Root<?> root, String pathExpression, String value) {
        return predicateBuildStrategies.stream()
                .filter(s -> s.canHandle(root, pathExpression, value))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Not supported entity property type: " + pathExpression));
    }


}
