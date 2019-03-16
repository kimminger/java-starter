package com.elderbyte.web;

import com.elderbyte.commons.exceptions.ArgumentNullException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.lang.Nullable;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import javax.validation.constraints.NotNull;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class QueryMapUtil {

    private static final Logger logger = LoggerFactory.getLogger(QueryMapUtil.class);

    /***************************************************************************
     *                                                                         *
     * Public API                                                              *
     *                                                                         *
     **************************************************************************/

    public static MultiValueMap<String, String> toQueryMap(@NotNull Object queryMapContainer){

        if(queryMapContainer == null) throw new ArgumentNullException("queryMapContainer");

        var properties = Arrays.asList(
                BeanUtils.getPropertyDescriptors(queryMapContainer.getClass())
        );

        var queryMap = new LinkedMultiValueMap<String, String>();

        properties.forEach(p -> {
                var name = p.getName();
                if(!name.equals("class")){
                    var values = readPropertyAsMultiString(queryMapContainer, p);
                    if(!values.isEmpty()){
                        queryMap.put(name, values);
                    }
                }
        });

        return queryMap;
    }

    /***************************************************************************
     *                                                                         *
     * Private methods                                                         *
     *                                                                         *
     **************************************************************************/

    /**
     * Reads the given property as a multi string value.
     *
     * int -> ["12"]
     * obj -> [obj.toString()]
     * [a,b] -> ["a","b"]
     */
    private static List<String> readPropertyAsMultiString(Object host, PropertyDescriptor p){
        var values = new ArrayList<String>();
        try {
            var type = p.getPropertyType();

            var valueObj = p.getReadMethod().invoke(host);

            if(Iterable.class.isAssignableFrom(type)){
                values.addAll(multiValues((Collection)valueObj));
            }else if (type.isArray()){
                values.addAll(multiValuesFromArray(valueObj));
            }else{
                var value = toQueryValueOrNull(valueObj);
                if(value != null){
                    values.add(value);
                }
            }
        }catch (Exception e){
            logger.warn("Failed to extract property!", e);
        }

        return values;
    }


    private static List<String> multiValues(@Nullable Iterable<?> iterable){

        var values = new ArrayList<String>(2);
        if(iterable != null){
            iterable.forEach(r -> {

                var v = toQueryValueOrNull(r);
                if(v != null){
                    values.add(v);
                }

            });
        }
        return values;
    }

    private static List<String> multiValuesFromArray(@Nullable Object array){
        var values = new ArrayList<String>(2);
        if (array != null && array.getClass().isArray()) {
            int length = Array.getLength(array);
            for (int i = 0; i < length; i ++) {
                Object arrayElement = Array.get(array, i);
                values.add(toQueryValueOrNull(arrayElement));
            }
        }
        return values;
    }

    private static String toQueryValueOrNull(@Nullable Object object){
        if(object == null) return null;
        return object.toString();
    }
}
