package com.elderbyte.web.client;

import com.elderbyte.web.rest.api.EndpointClient;

/**
 * Represents a web api endpoint.
 * I.e.:  /api/customers
 */
public abstract class ApiEndpoint {

    /***************************************************************************
     *                                                                         *
     * Fields                                                                  *
     *                                                                         *
     **************************************************************************/

    private final WebClientApi client;
    private final EndpointClient endpoint;

    /***************************************************************************
     *                                                                         *
     * Constructor                                                             *
     *                                                                         *
     **************************************************************************/

    protected ApiEndpoint(WebClientApi client, String endpoint) {
        this(client, new EndpointClientImpl(client, endpoint));
    }

    protected ApiEndpoint(WebClientApi client, EndpointClient endpoint) {
        this.client = client;
        this.endpoint = endpoint;
    }

    /***************************************************************************
     *                                                                         *
     * Properties                                                              *
     *                                                                         *
     **************************************************************************/

    /**
     * Get the underlying endpoint client
     */
    protected EndpointClient endpoint(){
        return endpoint;
    }

    /**
     * Get the underlying web client api.
     */
    protected WebClientApi client(){
        return client;
    }

}
