package com.elderbyte.spring.data.jpa.specification.builder;

import com.elderbyte.spring.data.jpa.specification.JpaPath;
import com.elderbyte.spring.data.jpa.specification.expressions.Expression;
import com.elderbyte.spring.data.jpa.specification.expressions.LogicExpression;
import com.elderbyte.spring.data.jpa.specification.expressions.ValueExpression;
import com.elderbyte.spring.data.jpa.specification.predicates.AutoPredicateProvider;
import com.elderbyte.spring.data.jpa.specification.predicates.PredicateProviderPathValue;
import com.elderbyte.spring.data.jpa.specification.predicates.PredicateProviderExpressionValue;
import com.elderbyte.spring.data.jpa.specification.predicates.PredicateProviderValue;
import com.elderbyte.spring.data.jpa.specification.sort.CustomOrderProvider;
import com.elderbyte.spring.data.jpa.specification.sort.OrderSpecTemplate;

import java.util.*;

import static java.util.stream.Collectors.toList;

public class SpecTemplateBuilder<T> {
    /***************************************************************************
     *                                                                         *
     * Static Builder                                                          *
     *                                                                         *
     **************************************************************************/

    public static <ST> SpecTemplateBuilder<ST> start(Class<ST> entity){
        return new SpecTemplateBuilder<>();
    }

    /***************************************************************************
     *                                                                         *
     * Fields                                                                  *
     *                                                                         *
     **************************************************************************/

    private PredicateProviderPathValue<T> defaultPredicateBuilder = new AutoPredicateProvider<>();

    private final List<QueryParamCriteria<T>> paramCriterias = new ArrayList<>();
    private final Map<String, CustomOrderProvider<T>> customSorts = new HashMap<>();

    /***************************************************************************
     *                                                                         *
     * Public API                                                              *
     *                                                                         *
     **************************************************************************/

    public SpecTemplateBuilder<T> defaultBuilder(PredicateProviderPathValue<T> defaultPredicateBuilder){
        this.defaultPredicateBuilder = defaultPredicateBuilder;
        return this;
    }

    public SpecTemplateBuilder<T> paramPathAny(String queryParam, String ...paths){

        var rules = Arrays.stream(paths)
                .map(p -> new QueryParamRulePath<T>(p))
                .map(qp -> (QueryParamRule<T>)qp)
                .map(ValueExpression::new)
                .collect(toList());

        var disjunction = LogicExpression.disjunction(rules);

        return paramRule(
                queryParam,
                disjunction
        );
    }

    public SpecTemplateBuilder<T> paramPath(String queryParam, String path){
        return paramRule(queryParam, new QueryParamRulePath<>(path));
    }

    public SpecTemplateBuilder<T> paramPathCustom(String queryParam, String path, PredicateProviderExpressionValue<T> customPredicateProvider){
        return paramCustom(
                queryParam,
                (root, cb, value) -> customPredicateProvider.buildPredicate(root, cb, JpaPath.resolve(root, path), value)
        );
    }

    public SpecTemplateBuilder<T> paramCustom(String queryParam, PredicateProviderValue<T> customPredicateProvider){
        return paramRule(queryParam, new QueryParamRuleCustomPredicate<>(customPredicateProvider));
    }


    public SpecTemplateBuilder<T> paramRule(String queryParam, QueryParamRule<T> rule){
        return paramRule(queryParam, new ValueExpression<>(rule));
    }

    public SpecTemplateBuilder<T> paramRule(String queryParam, Expression<QueryParamRule<T>> ruleExpression){
        return paramRule(new QueryParamRef(queryParam, false, null), ruleExpression);
    }

    public SpecTemplateBuilder<T> paramRule(QueryParamRef queryParam, Expression<QueryParamRule<T>> ruleExpression){
        return criteria(new QueryParamCriteria<T>(queryParam, ruleExpression));
    }

    public SpecTemplateBuilder<T> criteria(QueryParamCriteria<T> criteria){
        this.paramCriterias.add(criteria);
        return this;
    }

    public SpecTemplateBuilder<T> sortWith(String sortKey, CustomOrderProvider<T> customOrderProvider){
        customSorts.put(sortKey, customOrderProvider);
        return this;
    }


    /**
     * Builds a specification template based on the current builder configuration.
     */
    public SortedSpecTemplate<T> build(){
        return new SortedSpecTemplate<>(
                new QueryParamSpecificationTemplate<>(paramCriterias, this.defaultPredicateBuilder),
                new OrderSpecTemplate<>(customSorts),
                defaultPredicateBuilder
        );
    }



}
