package com.elderbyte.spring.data.jpa.specification;

import javax.persistence.criteria.From;
import javax.persistence.criteria.Path;
import java.util.Arrays;
import java.util.List;

public class JpaPathExpression {


    public static boolean isDisjunction(String pathExpression){
        return pathExpression.contains("|");
    }

    public static List<String> parseDisjunction(String pathExpression){
        return Arrays.asList(pathExpression.split("\\|"));
    }

    public static boolean isConcat(String pathExpression){
        return pathExpression.contains("@");
    }

    public static List<String> parseConcat(String pathExpression){
        return Arrays.asList(pathExpression.split("@"));
    }


    /**
     * Create {@link Path} for a search column identifier by recursively resolving joins.
     */
    public static <T,X> Path<X> resolve(From<X, T> root, String pathExpression) {
        if (!pathExpression.contains(".")) return root.get(pathExpression);
        final String[] parts = pathExpression.split("\\.", 2);
        return resolve(root.join(parts[0]), parts[1]);
    }

    public static List<String> splitPath(String pathExpression){
        final String[] parts = pathExpression.split("\\.");
        return Arrays.asList(parts);
    }

}
