package com.elderbyte.spring.data.jpa.specification;

import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.data.mapping.PropertyPath;
import org.springframework.lang.Nullable;

import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.criteria.*;
import javax.persistence.metamodel.Attribute;
import javax.persistence.metamodel.Bindable;
import javax.persistence.metamodel.ManagedType;
import javax.persistence.metamodel.PluralAttribute;
import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Member;
import java.util.*;

import static javax.persistence.metamodel.Attribute.PersistentAttributeType.*;
import static javax.persistence.metamodel.Attribute.PersistentAttributeType.ELEMENT_COLLECTION;

/**
 * Provides the ability to resolve a property path to a JPA criteria expression.
 *
 * This code is based on springs QueryUtils.
 */
public class JpaPath {

    /***************************************************************************
     *                                                                         *
     * Fields                                                                  *
     *                                                                         *
     **************************************************************************/

    private static final Map<Attribute.PersistentAttributeType, Class<? extends Annotation>> ASSOCIATION_TYPES;

    static {
        var persistentAttributeTypes = new HashMap<Attribute.PersistentAttributeType, Class<? extends Annotation>>();
        persistentAttributeTypes.put(ONE_TO_ONE, OneToOne.class);
        persistentAttributeTypes.put(ONE_TO_MANY, null);
        persistentAttributeTypes.put(MANY_TO_ONE, ManyToOne.class);
        persistentAttributeTypes.put(MANY_TO_MANY, null);
        persistentAttributeTypes.put(ELEMENT_COLLECTION, null);


        ASSOCIATION_TYPES = Collections.unmodifiableMap(persistentAttributeTypes);
    }

    /***************************************************************************
     *                                                                         *
     * Public API                                                              *
     *                                                                         *
     **************************************************************************/

    /**
     * Create {@link Path} for a search column identifier by recursively resolving joins.
     */
    public static <T,X, Y> Expression<Y> resolve(From<X, T> root, String pathExpression) {

        PropertyPath property = PropertyPath.from(pathExpression, root.getJavaType());
        return toExpressionRecursively(root, property);
    }

    /***************************************************************************
     *                                                                         *
     * Private methods                                                         *
     *                                                                         *
     **************************************************************************/


    private static <T> Expression<T> toExpressionRecursively(From<?,?> from, PropertyPath property) {

        Bindable<?> propertyPathModel;
        Bindable<?> model = from.getModel();
        String segment = property.getSegment();

        if (model instanceof ManagedType) {
            propertyPathModel = (Bindable<?>) ((ManagedType<?>) model).getAttribute(segment);
        } else {
            propertyPathModel = from.get(segment).getModel();
        }

        var joinType = decideJoinType(propertyPathModel, model instanceof PluralAttribute, !property.hasNext());
        // JoinType joinType = null;

        if(joinType != null && !isAlreadyFetched(from, segment, joinType)){
            Join<?, ?> join = getOrCreateJoin(from, segment, joinType);
            return (Expression<T>) (property.hasNext() ? toExpressionRecursively(join, property.next()) : join);
        } else {
            Path<Object> path = from.get(segment);
            return (Expression<T>) (property.hasNext() ? toExpressionRecursively(path, property.next()) : path);
        }
    }


    static Expression<Object> toExpressionRecursively(Path<Object> path, PropertyPath property) {

        Path<Object> result = path.get(property.getSegment()); //Illegal attempt to dereference path source [null.propertyname] of basic type
        return property.hasNext() ? toExpressionRecursively(result, property.next()) : result;
    }

    /**
     * Returns whether the given {@code propertyPathModel} requires the creation of a join. This is the case if we find a
     * optional association.
     *
     * @param propertyPathModel may be {@literal null}.
     * @param isPluralAttribute is the attribute of Collection type?
     * @param isLeafProperty is this the final property navigated by a {@link PropertyPath}?
     * @return wether an outer join is to be used for integrating this attribute in a query.
     */
    private static JoinType decideJoinType(@Nullable Bindable<?> propertyPathModel, boolean isPluralAttribute,
                                        boolean isLeafProperty) {

        if (propertyPathModel == null && isPluralAttribute) {
            return JoinType.LEFT;
        }

        if (!(propertyPathModel instanceof Attribute)) {
            return null; // no join
        }

        var attribute = (Attribute<?, ?>) propertyPathModel;

        if (!ASSOCIATION_TYPES.containsKey(attribute.getPersistentAttributeType())) {
            return null; // no join
        }

        if (isLeafProperty) { // && !attribute.isCollection()
            return null; // no join
        }

        var associationAnnotation = ASSOCIATION_TYPES.get(attribute.getPersistentAttributeType());

        if (associationAnnotation == null) {
            return JoinType.LEFT;
        }

        Member member = attribute.getJavaMember();

        if (!(member instanceof AnnotatedElement)) {
            return JoinType.LEFT;
        }

        var annotation = AnnotationUtils.getAnnotation((AnnotatedElement) member, associationAnnotation);
        var isOptional = annotation == null ? true : (boolean) AnnotationUtils.getValue(annotation, "optional");

        return isOptional ? JoinType.LEFT : JoinType.INNER;
    }

    /**
     * Returns an existing join for the given attribute if one already exists or creates a new one if not.
     *
     * @param from the {@link From} to get the current joins from.
     * @param attribute the {@link Attribute} to look for in the current joins.
     * @return will never be {@literal null}.
     */
    private static Join<?, ?> getOrCreateJoin(From<?, ?> from, String attribute, JoinType joinType) {

        for (Join<?, ?> join : from.getJoins()) {

            boolean sameName = join.getAttribute().getName().equals(attribute);

            if (sameName && join.getJoinType().equals(joinType)) {
                return join;
            }
        }

        return from.join(attribute, joinType);
    }

    /**
     * Return whether the given {@link From} contains a fetch declaration for the attribute with the given name.
     *
     * @param from the {@link From} to check for fetches.
     * @param attribute the attribute name to check.
     * @return
     */
    private static boolean isAlreadyFetched(From<?, ?> from, String attribute, JoinType joinType) {

        for (Fetch<?, ?> fetch : from.getFetches()) {

            boolean sameName = fetch.getAttribute().getName().equals(attribute);

            if (sameName && fetch.getJoinType().equals(joinType)) {
                return true;
            }
        }

        return false;
    }

}
