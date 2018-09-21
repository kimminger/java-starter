package com.elderbyte.spring.data.jpa.specification.predicates.support;

import com.elderbyte.spring.data.jpa.specification.JpaPathExpression;
import com.elderbyte.spring.data.jpa.specification.MatchablePredicateBuildStrategy;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;

public class StringPredicateBuildStrategy implements MatchablePredicateBuildStrategy {

    @Override
    public boolean canHandle(Root<?> root, String pathExpression, String value) {

        var paths = new ArrayList<Path<?>>();

        if(JpaPathExpression.isConcat(pathExpression)){
            paths.addAll(parseConcat(root, pathExpression));
        }else{
            paths.add(JpaPathExpression.resolve(root, pathExpression));
        }

        return paths.stream().allMatch(p -> String.class.isAssignableFrom(p.getJavaType()));
    }

    @Override
    public Predicate buildPredicate(Root<?> root, String pathExpression, CriteriaBuilder cb, String value) {

        Expression<String> expression;

        if(JpaPathExpression.isConcat(pathExpression)){
            expression = buildConcat(root, pathExpression, cb);
        }else{
            var path = JpaPathExpression.resolve(root, pathExpression);
            expression = path.as(String.class);
        }

        return StringPredicateBuilder.matchSubstringSyntax(expression, cb, value);
    }

    private Expression<String> buildConcat(Root<?> root, String pathExpression, CriteriaBuilder cb){
        var concatExpressions = parseConcat(root, pathExpression).stream()
                                .map(cp -> cp.as(String.class))
                                .collect(toList());

        if(concatExpressions.size() == 2){
            var ex1 = concatExpressions.get(0);
            var ex2 = concatExpressions.get(1);

            return cb.concat(cb.concat(ex1, " "), ex2);
        }else{
            throw new IllegalArgumentException("We currently only support a single concat, but it was: "
                    + pathExpression + " which led to "+concatExpressions.size()+" parts!");
        }
    }

    private List<Path<?>> parseConcat(Root<?> root, String pathExpression){
        return JpaPathExpression.parseConcat(pathExpression).stream()
                        .map(p -> JpaPathExpression.resolve(root, p))
                        .collect(toList());
    }
}
