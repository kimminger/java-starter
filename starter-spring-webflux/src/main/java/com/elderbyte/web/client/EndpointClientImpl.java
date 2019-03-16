package com.elderbyte.web.client;

import com.elderbyte.web.rest.api.EndpointClient;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.reactive.ClientHttpRequest;
import org.springframework.web.reactive.function.BodyInserter;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.util.UriBuilder;
import reactor.core.publisher.Mono;

import java.util.function.Function;

/**
 * Represents a WebClientApi which is scoped to a specific sub resource
 * endpoint.
 *
 * Scopes all requests like: http://{baseClient-url}/{endpoint}/
 *
 */
public class EndpointClientImpl implements EndpointClient {

    /***************************************************************************
     *                                                                         *
     * Fields                                                                  *
     *                                                                         *
     **************************************************************************/

    private final WebClientApi baseClient;
    private final String endpoint;

    /***************************************************************************
     *                                                                         *
     * Constructor                                                             *
     *                                                                         *
     **************************************************************************/

    /**
     * Creates a new RestEndpoint
     */
    public EndpointClientImpl(WebClientApi baseClient, String endpoint) {
        this.baseClient = baseClient;
        this.endpoint = endpoint;
    }

    /***************************************************************************
     *                                                                         *
     * Properties                                                              *
     *                                                                         *
     **************************************************************************/

    public String getEndpointPath() {
        return endpoint;
    }

    /***************************************************************************
     *                                                                         *
     * Protected Http retrieve API (Generics)                                  *
     *                                                                         *
     **************************************************************************/

    public  <T> Mono<T> delete(
            Function<UriBuilder, UriBuilder> uriBuilder,
            ParameterizedTypeReference<T> responseBodyType,
            RequestOptions options
    ){
        return retrieve(HttpMethod.DELETE, uriBuilder, BodyInserters.empty(), responseBodyType, options);
    }

    public <T> Mono<T> get(
            Function<UriBuilder, UriBuilder> uriBuilder,
            ParameterizedTypeReference<T> responseBodyType,
            RequestOptions options
    ){
        return retrieve(HttpMethod.GET, uriBuilder, BodyInserters.empty(), responseBodyType, options);
    }

    public <T> Mono<T> post(
            Function<UriBuilder, UriBuilder> uriBuilder,
            BodyInserter<?, ? super ClientHttpRequest> requestBody,
            ParameterizedTypeReference<T> responseBodyType,
            RequestOptions options
    ){
        return retrieve(HttpMethod.POST, uriBuilder, requestBody, responseBodyType, options);
    }

    public <T> Mono<T> put(
            Function<UriBuilder, UriBuilder> uriBuilder,
            BodyInserter<?, ? super ClientHttpRequest> requestBody,
            ParameterizedTypeReference<T> responseBodyType,
            RequestOptions options
    ){
        return retrieve(HttpMethod.PUT, uriBuilder, requestBody, responseBodyType, options);
    }

    /***************************************************************************
     *                                                                         *
     * Protected Http retrieve API                                             *
     *                                                                         *
     **************************************************************************/


    public <T> Mono<T> delete(
            Function<UriBuilder, UriBuilder> uriBuilder,
            Class<T> responseBodyType,
            RequestOptions options
    ){
        return retrieve(HttpMethod.DELETE, uriBuilder, BodyInserters.empty(), responseBodyType, options);
    }

    public <T> Mono<T> get(
            Function<UriBuilder, UriBuilder> uriBuilder,
            Class<T> responseBodyType
    ){
        return retrieve(HttpMethod.GET, uriBuilder, BodyInserters.empty(), responseBodyType, RequestOptions.empty());
    }

    public <T> Mono<T> get(
            Function<UriBuilder, UriBuilder> uriBuilder,
            Class<T> responseBodyType,
            RequestOptions options
    ){
        return retrieve(HttpMethod.GET, uriBuilder, BodyInserters.empty(), responseBodyType, options);
    }

    public <T> Mono<T> post(
            Function<UriBuilder, UriBuilder> uriBuilder,
            BodyInserter<?, ? super ClientHttpRequest> requestBody,
            Class<T> responseBodyType,
            RequestOptions options
    ){
        return retrieve(HttpMethod.POST, uriBuilder, requestBody, responseBodyType, options);
    }

    public <T> Mono<T> put(
            Function<UriBuilder, UriBuilder> uriBuilder,
            BodyInserter<?, ? super ClientHttpRequest> requestBody,
            Class<T> responseBodyType,
            RequestOptions options
    ){
        return retrieve(HttpMethod.PUT, uriBuilder, requestBody, responseBodyType, options);
    }

    /***************************************************************************
     *                                                                         *
     * Private methods                                                         *
     *                                                                         *
     **************************************************************************/

    public <T> Mono<T> retrieve(
            HttpMethod method,
            Function<UriBuilder, UriBuilder> uriBuilder,
            BodyInserter<?, ? super ClientHttpRequest> requestBody,
            ParameterizedTypeReference<T> responseBodyType,
            RequestOptions options
    ){
        return baseClient.retrieve(
                method,
                b -> uriBuilder.apply(b.path(endpoint)),
                requestBody,
                responseBodyType,
                options
        );
    }

    public <T> Mono<T> retrieve(
            HttpMethod method,
            Function<UriBuilder, UriBuilder> uriBuilder,
            BodyInserter<?, ? super ClientHttpRequest> requestBody,
            Class<T> responseBodyType,
            RequestOptions options
    ){
        return baseClient.retrieve(
                method,
                b -> uriBuilder.apply(b.path(endpoint)),
                requestBody,
                responseBodyType,
                options
        );
    }
}
