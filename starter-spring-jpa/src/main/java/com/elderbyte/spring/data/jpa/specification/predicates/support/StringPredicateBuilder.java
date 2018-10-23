package com.elderbyte.spring.data.jpa.specification.predicates.support;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;

public final class StringPredicateBuilder {

    /* *************************************************************************
     *                                                                         *
     * Public API                                                              *
     *                                                                         *
     **************************************************************************/

    public static Predicate matchSubstringSyntax(Expression<String> expression, CriteriaBuilder cb, String value) {

        var wordStart = value.trim().startsWith("\"");
        var wordEnd = value.trim().endsWith("\"");

        value = value.replaceAll("\"", ""); // Remove quotes syntax

        if(!wordStart && ! wordEnd){
           return matchWordsInOrder(expression, cb, value);
        } else {

            if(wordStart && wordEnd){
                // match as whole 'word'
                return matchWord(expression, cb, value);
            }else if (wordStart){
                // only start must be exact word
                return matchWordStart(expression, cb, value);
            }else{ // wordEnd true
                // only end must be exact word
                return matchWordEnd(expression, cb, value);
            }
        }
    }

    public static Predicate matchWordsInOrder(Expression<String> expression, CriteriaBuilder cb, String value) {
        value = value.replaceAll("\\s+", "%");
        return likeIgnoreCase(expression, cb, "%" + value + "%");
    }

    public static Predicate matchInPlace(Expression<String> expression, CriteriaBuilder cb, String value) {
        return likeIgnoreCase(expression, cb, "%" + value + "%");
    }

    public static Predicate equalIgnoreCase(Expression<String> expression, CriteriaBuilder cb, Object value) {
        if (value == null)
            return cb.isNull(expression);
        return cb.equal(cb.lower(expression), value.toString().toLowerCase());
    }

    /* *************************************************************************
     *                                                                         *
     * Private methods                                                         *
     *                                                                         *
     **************************************************************************/


    private static Predicate matchWordEnd(Expression<String> expression, CriteriaBuilder cb, String value) {
        return cb.or(
                likeIgnoreCase(expression, cb, "%" + value),   // at end
                likeIgnoreCase(expression, cb, "%" + value + " %") // In middle
        );
    }

    private static Predicate matchWordStart(Expression<String> expression, CriteriaBuilder cb, String value) {
        return cb.or(
                likeIgnoreCase(expression, cb, value + "%"),   // at beginning
                likeIgnoreCase(expression, cb, "% " + value + "%") // In middle
        );
    }

    private static Predicate matchWord(Expression<String> expression, CriteriaBuilder cb, String value) {
        return cb.or(
              equalIgnoreCase(expression, cb, value),  // beginning and end
              likeIgnoreCase(expression, cb, value + " %"),   // at beginning
              likeIgnoreCase(expression, cb, "% " + value),     // at end
              likeIgnoreCase(expression, cb, "% " + value + " %")     // in middle of sentence
        );
    }

    private static Predicate likeIgnoreCase(Expression<String> expression, CriteriaBuilder cb, String value) {
        return cb.like(cb.lower(expression), value.toLowerCase());
    }

}
