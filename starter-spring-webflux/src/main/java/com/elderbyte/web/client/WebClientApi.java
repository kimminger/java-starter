package com.elderbyte.web.client;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.reactive.ClientHttpRequest;
import org.springframework.web.reactive.function.BodyInserter;
import org.springframework.web.util.UriBuilder;
import reactor.core.publisher.Mono;

import java.util.function.Function;

public interface WebClientApi {

    <T> Mono<T> delete(
            Function<UriBuilder, UriBuilder> uriBuilder,
            ParameterizedTypeReference<T> responseBodyType,
            RequestOptions options
    );

    <T> Mono<T> get(
            Function<UriBuilder, UriBuilder> uriBuilder,
            ParameterizedTypeReference<T> responseBodyType,
            RequestOptions options
    );

    <T> Mono<T> post(
            Function<UriBuilder, UriBuilder> uriBuilder,
            BodyInserter<?, ? super ClientHttpRequest> requestBody,
            ParameterizedTypeReference<T> responseBodyType,
            RequestOptions options
    );

    default <T> Mono<T> post(
            String pathPart,
            BodyInserter<?, ? super ClientHttpRequest> requestBody,
            ParameterizedTypeReference<T> responseBodyType,
            RequestOptions options
    ) {
        return post(b -> b.pathSegment(pathPart), requestBody, responseBodyType, options);
    }

    <T> Mono<T> put(
            Function<UriBuilder, UriBuilder> uriBuilder,
            BodyInserter<?, ? super ClientHttpRequest> requestBody,
            ParameterizedTypeReference<T> responseBodyType,
            RequestOptions options
    );

    <T> Mono<T> retrieve(
            HttpMethod method,
            Function<UriBuilder, UriBuilder> uriBuilder,
            BodyInserter<?, ? super ClientHttpRequest> requestBody,
            ParameterizedTypeReference<T> responseBodyType,
            RequestOptions options
    );

    <T> Mono<T> delete(
            Function<UriBuilder, UriBuilder> uriBuilder,
            Class<T> responseBodyType,
            RequestOptions options
    );

    default <T> Mono<T> delete(
            Function<UriBuilder, UriBuilder> uriBuilder,
            Class<T> responseBodyType
    ){
        return delete(uriBuilder, responseBodyType, RequestOptions.empty());
    }

    default <T> Mono<T> delete(
            Class<T> responseBodyType,
            RequestOptions options
    ){
        return delete(b -> b, responseBodyType, options);
    }

    default <T> Mono<T> delete(
            ParameterizedTypeReference<T> responseBodyType,
            RequestOptions options
    ){
        return delete(b -> b, responseBodyType, options);
    }

    <T> Mono<T> get(
            Function<UriBuilder, UriBuilder> uriBuilder,
            Class<T> responseBodyType
    );

    <T> Mono<T> get(
            Function<UriBuilder, UriBuilder> uriBuilder,
            Class<T> responseBodyType,
            RequestOptions options
    );

    default <T> Mono<T> get(
            Class<T> responseBodyType
    ){
        return get(b -> b, responseBodyType);
    }

    default <T> Mono<T> get(
            Class<T> responseBodyType,
            RequestOptions options
    ){
        return get(b -> b, responseBodyType, options);
    }

    default <T> Mono<T> get(
            String pathPart,
            Class<T> responseType
    ){
        return get(pathPart, RequestOptions.empty(), responseType);
    }

    default <T> Mono<T> get(
            String pathPart,
            RequestOptions options,
            Class<T> responseType
    ){
        return get(
                        uri -> uri.pathSegment(pathPart),
                        responseType,
                        options
                );
    }

    default <T> Mono<T> post(
            String pathPart,
            BodyInserter<?, ? super ClientHttpRequest> requestBody,
            Class<T> responseBodyType,
            RequestOptions options
    ){
        return post(b -> b.pathSegment(pathPart), requestBody, responseBodyType, options);
    }

    default <T> Mono<T> post(
            BodyInserter<?, ? super ClientHttpRequest> requestBody,
            Class<T> responseBodyType,
            RequestOptions options
    ){
        return post(b -> b, requestBody, responseBodyType, options);
    }

    <T> Mono<T> post(
            Function<UriBuilder, UriBuilder> uriBuilder,
            BodyInserter<?, ? super ClientHttpRequest> requestBody,
            Class<T> responseBodyType,
            RequestOptions options
    );


    default <T> Mono<T> put(
            BodyInserter<?, ? super ClientHttpRequest> requestBody,
            Class<T> responseBodyType,
            RequestOptions options
    ){
        return put(b -> b, requestBody, responseBodyType, options);
    }

    <T> Mono<T> put(
            Function<UriBuilder, UriBuilder> uriBuilder,
            BodyInserter<?, ? super ClientHttpRequest> requestBody,
            Class<T> responseBodyType,
            RequestOptions options
    );

    <T> Mono<T> retrieve(
            HttpMethod method,
            Function<UriBuilder, UriBuilder> uriBuilder,
            BodyInserter<?, ? super ClientHttpRequest> requestBody,
            Class<T> responseBodyType,
            RequestOptions options
    );
}
