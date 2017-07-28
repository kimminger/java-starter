package com.elderbyte.commons.exceptions;

/**
 * Thrown when an entity is not found.
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
