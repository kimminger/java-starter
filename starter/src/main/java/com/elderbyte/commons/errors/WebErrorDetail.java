package com.elderbyte.commons.errors;


import com.elderbyte.commons.exceptions.ExceptionUtil;

public class WebErrorDetail {

    /***************************************************************************
     *                                                                         *
     * Static Builders                                                         *
     *                                                                         *
     **************************************************************************/

    public static WebErrorDetail build(
            int status,
            String error,
            Throwable exception,
            String request
    ){
        return build(
                status,
                error,
                exception,
                request,
                null
        );
    }

    public static WebErrorDetail build(
            int status,
            String error,
            Throwable exception,
            String request,
            String authentication
    ){

        return new WebErrorDetail(
                status,
                error,
                exception.getClass().getSimpleName(),
                ExceptionUtil.aggregateMessages(exception),
                request,
                authentication
        );
    }

    /***************************************************************************
     *                                                                         *
     * Fields                                                                  *
     *                                                                         *
     **************************************************************************/

    /**
     * The error name
     */
    private final String error;

    /**
     * The error status code
     */
    private final int status;

    /**
     * Type of error (usually the Exception class)
     */
    private final String type;

    /**
     * A short message what went wrong
     */
    private final String message;

    /**
     * The offending request in the form: HTTP-METHOD /MY/PATH/
     */
    private final String request;

    /**
     * Information about who caused the request if available
     */
    private final String authentication;

    /***************************************************************************
     *                                                                         *
     * Constructors                                                            *
     *                                                                         *
     **************************************************************************/

    public WebErrorDetail(
            int status,
            String error,
            String type,
            String message,
            String request,
            String authentication) {
        this.error = error;
        this.status = status;
        this.type = type;
        this.message = message;
        this.request = request;
        this.authentication = authentication;
    }

    /***************************************************************************
     *                                                                         *
     * Public API                                                              *
     *                                                                         *
     **************************************************************************/

    @Override
    public String toString() {
        return getType() + " at " + request + ": " + getMessage();
    }

    /***************************************************************************
     *                                                                         *
     * Properties                                                              *
     *                                                                         *
     **************************************************************************/

    public String getError() {
        return error;
    }

    public int getStatus() {
        return status;
    }

    public String getType() {
        return type;
    }

    public String getMessage() {
        return message;
    }

    public String getRequest() {
        return request;
    }

    public String getAuthentication() {
        return authentication;
    }
}
