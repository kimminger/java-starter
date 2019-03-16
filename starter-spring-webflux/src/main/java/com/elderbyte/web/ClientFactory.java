package com.elderbyte.web;

import org.springframework.web.reactive.function.client.WebClient;

import java.util.function.Function;

public class ClientFactory<T> {

    /***************************************************************************
     *                                                                         *
     * Static Builder                                                          *
     *                                                                         *
     **************************************************************************/

    public static <TS> ClientFactory<TS> from(
            WebClient.Builder webClientBuilder,
            Function<WebClient, TS> clientApiBuilder){
        return new ClientFactory<>(webClientBuilder, clientApiBuilder);
    }

    /***************************************************************************
     *                                                                         *
     * Fields                                                                  *
     *                                                                         *
     **************************************************************************/

    private final WebClient.Builder webClientBuilder;
    private final Function<WebClient, T> clientApiBuilder;

    /***************************************************************************
     *                                                                         *
     * Constructor                                                             *
     *                                                                         *
     **************************************************************************/

    /**
     * Creates a new ClientFactory
     */
    private ClientFactory(
            WebClient.Builder webClientBuilder,
            Function<WebClient, T> clientApiBuilder) {
        this.webClientBuilder = webClientBuilder;
        this.clientApiBuilder = clientApiBuilder;
    }

    /***************************************************************************
     *                                                                         *
     * Public API                                                              *
     *                                                                         *
     **************************************************************************/

    /**
     * Build a new Client with the default properties
     */
    public T build(){
        return build(b -> b);
    }

    /**
     * Build a new Client with custom default properties.
     * I.e. apply custom request filters / interceptors to the client.
     */
    public T build(Function<WebClient.Builder, WebClient.Builder> builder){
        return clientApiBuilder.apply(
                builder.apply(webClientBuilder).build()
        );
    }
}
