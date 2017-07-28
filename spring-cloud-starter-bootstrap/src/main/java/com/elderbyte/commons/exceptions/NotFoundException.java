package com.elderbyte.commons.exceptions;

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
