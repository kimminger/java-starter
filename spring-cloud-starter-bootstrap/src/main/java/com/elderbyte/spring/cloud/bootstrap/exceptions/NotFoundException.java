package com.elderbyte.spring.cloud.bootstrap.exceptions;

/**
 * Is thrown when an entity is not found.
 */
public class NotFoundException extends RuntimeException {

    /***************************************************************************
     *                                                                         *
     * Constructors                                                            *
     *                                                                         *
     **************************************************************************/

    public NotFoundException(String message) {
        super(message);
    }


}
