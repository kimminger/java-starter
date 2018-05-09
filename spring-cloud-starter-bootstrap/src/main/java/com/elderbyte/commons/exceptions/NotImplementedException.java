package com.elderbyte.commons.exceptions;

/**
 * Thrown when a feature is used which is not yet available.
 */
public class NotImplementedException extends RuntimeException {

    public NotImplementedException(String feature){
        super(feature + " is not implemented yet!");
    }

}
