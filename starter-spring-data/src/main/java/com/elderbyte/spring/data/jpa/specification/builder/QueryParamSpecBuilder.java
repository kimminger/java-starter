package com.elderbyte.spring.data.jpa.specification.builder;

import com.elderbyte.spring.data.jpa.specification.JpaPath;
import com.elderbyte.spring.data.jpa.specification.expressions.LogicExpression;
import com.elderbyte.spring.data.jpa.specification.expressions.ValueExpression;
import com.elderbyte.spring.data.jpa.specification.predicates.*;
import com.elderbyte.spring.data.jpa.specification.SpecificationPaged;
import com.elderbyte.spring.data.jpa.specification.SpecificationPagedImpl;
import com.elderbyte.spring.data.jpa.specification.expressions.Expression;
import com.elderbyte.spring.data.jpa.specification.query.QueryHook;
import com.elderbyte.spring.data.jpa.specification.sort.DynamicOrderBySpecification;
import com.elderbyte.spring.data.jpa.specification.sort.OrderSpecTemplate;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import java.util.*;

public class QueryParamSpecBuilder<T> {

    /***************************************************************************
     *                                                                         *
     * Fields                                                                  *
     *                                                                         *
     **************************************************************************/

    private final QueryParamSpecificationTemplate<T> filterTemplate;
    private final OrderSpecTemplate<T> sortTemplate;
    private final List<QueryHook<T>> hooks = new ArrayList<>();
    private final PredicateProviderPathValue<T> defaultPredicateBuilder;

    private Expression<PredicateProvider<T>> staticPredicateExpression = null;
    private boolean staticPredicateExpressionOr = false;

    /***************************************************************************
     *                                                                         *
     * Constructor                                                             *
     *                                                                         *
     **************************************************************************/

    public QueryParamSpecBuilder(
            QueryParamSpecificationTemplate<T> filterTemplate,
            OrderSpecTemplate<T> orderSpecTemplate,
            PredicateProviderPathValue<T> defaultPredicateBuilder){
        this.filterTemplate = filterTemplate;
        this.sortTemplate = orderSpecTemplate;
        this.defaultPredicateBuilder = defaultPredicateBuilder;
    }

    /***************************************************************************
     *                                                                         *
     * Public API                                                              *
     *                                                                         *
     **************************************************************************/

    /**
     * Marks this query as distinct
     */
    public QueryParamSpecBuilder<T> distinct() {
        configureQuery((root, qry, cb) -> qry.distinct(true));
        return this;
    }

    /**
     * Provides low level access to the JPA query primitives
     */
    public QueryParamSpecBuilder<T> configureQuery(QueryHook<T> queryHook){
        hooks.add(queryHook);
        return this;
    }

    /**
     * Adds a global equality predicate.
     */
    public QueryParamSpecBuilder<T> andEquals(String path, Object value){
        and((root, cb) -> cb.equal(JpaPath.resolve(root, path), value));
        return this;
    }

    /**
     * The specified attribute must be a member of the given static values
     */
    public QueryParamSpecBuilder<T> andIn(String path, Collection<?> values){
        and((root, cb) -> JpaPath.resolve(root, path).in(values));
        return this;
    }

    /**
     * The specified attribute must be a member of the given static values
     */
    public QueryParamSpecBuilder<T> andNotIn(String path, Collection<?> values){
        and((root, cb) -> cb.not(JpaPath.resolve(root, path).in(values)));
        return this;
    }

    /**
     * Adds a global equality predicate which uses the dynamic predicate builder.
     */
    public QueryParamSpecBuilder<T> andMatches(String path, String value){

        var matchPathValuePredicate = new PredicateProviderPathValueAdapter<>(path, value, defaultPredicateBuilder);
        and(matchPathValuePredicate);

        return this;
    }

    public QueryParamSpecBuilder<T> and(String path, PredicateProviderExpression<T> predicate){
        and((root, cb) -> predicate.buildPredicate(root, cb, JpaPath.resolve(root, path)));
        return this;
    }

    public QueryParamSpecBuilder<T> and(PredicateProvider<T> predicate){
        and(new ValueExpression<>(predicate));
        return this;
    }

    public QueryParamSpecBuilder<T> or(PredicateProvider<T> predicate){
        or(new ValueExpression<>(predicate));
        return this;
    }

    public QueryParamSpecBuilder<T> and(Expression<PredicateProvider<T>> predicateExpression){
        if(staticPredicateExpression != null){
            staticPredicateExpression = LogicExpression.and(staticPredicateExpression, predicateExpression);
        }else{
            staticPredicateExpressionOr = false;
            staticPredicateExpression = predicateExpression;
        }
        return this;
    }

    public QueryParamSpecBuilder<T> or(Expression<PredicateProvider<T>> predicateExpression){
        if(staticPredicateExpression != null){
            staticPredicateExpression = LogicExpression.or(staticPredicateExpression, predicateExpression);
        }else{
            staticPredicateExpressionOr = true;
            staticPredicateExpression = predicateExpression;
        }
        return this;
    }


    /***************************************************************************
     *                                                                         *
     * Build Specification                                                     *
     *                                                                         *
     **************************************************************************/


    public Specification<T> buildMap(Map<String, String[]> queryParams){
        return build(convertParams(queryParams));
    }

    public Specification<T> buildMap(Map<String, String[]> queryParams, Sort sort){
        return build(
                convertParams(queryParams),
                sort
        );
    }

    public SpecificationPaged<T> buildMap(Map<String, String[]> queryParams, Pageable pageable){
        return build(
                convertParams(queryParams),
                pageable
        );
    }

    public Specification<T> build(Pageable pageable){
        return build(new HashMap<>(), pageable);
    }

    public SpecificationPaged<T> build(Map<String, ? extends Collection<String>> queryParams, Pageable pageable){

        Specification<T> sortedSpec = build(queryParams, pageable.getSort());

        return new SpecificationPagedImpl<>(
                sortedSpec,
                PageRequest.of(pageable.getPageNumber(), pageable.getPageSize())
        );
    }

    public Specification<T> build( Sort sort){
        return build(new HashMap<>(), sort);
    }

    public Specification<T> build(Map<String, ? extends Collection<String>> queryParams, Sort sort){
        Specification<T> spec = build(queryParams);
        return new DynamicOrderBySpecification<>(spec, sortTemplate, sort);
    }

    public Specification<T> build(){
        return build(new HashMap<>());
    }

    public Specification<T> build(Map<String, ? extends Collection<String>> queryParams){

        Expression<PredicateProvider<T>> expression = filterTemplate.resolve(queryParams);

        if(staticPredicateExpressionOr){
            expression = LogicExpression.or(expression, staticPredicateExpression);
        }else{
            expression = LogicExpression.and(expression, staticPredicateExpression);
        }

        return finalize(new PredicateExpressionSpecification<>(expression));
    }

    /***************************************************************************
     *                                                                         *
     * Private methods                                                         *
     *                                                                         *
     **************************************************************************/

    private Specification<T> finalize(Specification<T> specification){
        return new SpecificationHook<>(specification, hooks);
    }

    private Map<String, ? extends Collection<String>> convertParams(Map<String, String[]> queryParams){
        return QueryParamsBuilder.start().merge(queryParams).build();
    }
}
