package com.elderbyte.commons.utils;

import com.elderbyte.commons.exceptions.NotSupportedException;

import java.math.BigDecimal;

public final class NumberUtil {

    public static <T extends Number> T parseNumber(String numberString, Class<T> type){

        if(isInteger(type)){
            return (T) (Integer) Integer.parseInt(numberString);
        }else if(isLong(type)){
            return (T) (Long) Long.parseLong(numberString);
        }else if(isDouble(type)){
            return (T) (Double) Double.parseDouble(numberString);
        }else if(isShort(type)){
            return (T) (Short) Short.parseShort(numberString);
        }else if(isFloat(type)){
            return (T) (Float) Float.parseFloat(numberString);
        }else if(isBigDecimal(type)){
            return (T) BigDecimal.valueOf(Double.parseDouble(numberString));
        }else{
            throw new NotSupportedException("Unknown number type: " + type);
        }
    }

    public static boolean isNumeric(Class<?> type){
        return isInteger(type)
                || isLong(type)
                || isDouble(type)
                || isShort(type)
                || isFloat(type)
                || isBigDecimal(type);
    }

    public static boolean isShort(Class<?> type){
        return (Short.class.isAssignableFrom(type) || short.class.isAssignableFrom(type));
    }

    public static boolean isFloat(Class<?> type){
        return (Float.class.isAssignableFrom(type) || float.class.isAssignableFrom(type));
    }

    public static boolean isInteger(Class<?> type){
        return (Integer.class.isAssignableFrom(type) || int.class.isAssignableFrom(type));
    }

    public static boolean isLong(Class<?> type){
        return (Long.class.isAssignableFrom(type) || long.class.isAssignableFrom(type));
    }

    public static boolean isDouble(Class<?> type){
        return (Double.class.isAssignableFrom(type) || double.class.isAssignableFrom(type));
    }

    public static boolean isBigDecimal(Class<?> type){
        return (BigDecimal.class.isAssignableFrom(type));
    }

}
