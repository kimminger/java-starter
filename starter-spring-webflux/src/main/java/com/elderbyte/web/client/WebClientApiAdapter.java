package com.elderbyte.web.client;

import com.elderbyte.commons.exceptions.ArgumentNullException;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.reactive.ClientHttpRequest;
import org.springframework.web.reactive.function.BodyInserter;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.util.UriBuilder;
import reactor.core.publisher.Mono;

import java.util.function.Function;

/**
 * Provides an easy but extensible api for the WebClient.
 * Additionally, configures mappings and improved type wrappers.
 */
public class WebClientApiAdapter implements WebClientApi {

    public static WebClientApi from(WebClient client){
        return new WebClientApiAdapter(client);
    }

    /***************************************************************************
     *                                                                         *
     * Fields                                                                  *
     *                                                                         *
     **************************************************************************/

    private final WebClient client;

    /***************************************************************************
     *                                                                         *
     * Constructor                                                             *
     *                                                                         *
     **************************************************************************/

    /**
     * Creates a new RestEndpoint
     */
    public WebClientApiAdapter(WebClient client) {
        if(client == null) throw new ArgumentNullException("client");
        this.client = client;
    }

    /***************************************************************************
     *                                                                         *
     * Public API                                                              *
     *                                                                         *
     **************************************************************************/

    /***************************************************************************
     *                                                                         *
     * Protected Http retrieve API (Generics)                                  *
     *                                                                         *
     **************************************************************************/


    @Override
    public <T> Mono<T> delete(
            Function<UriBuilder, UriBuilder> uriBuilder,
            ParameterizedTypeReference<T> responseBodyType,
            RequestOptions options
    ){
        return retrieve(HttpMethod.DELETE, uriBuilder, BodyInserters.empty(), responseBodyType, options);
    }

    @Override
    public <T> Mono<T> get(
            Function<UriBuilder, UriBuilder> uriBuilder,
            ParameterizedTypeReference<T> responseBodyType,
            RequestOptions options
    ){
        return retrieve(HttpMethod.GET, uriBuilder, BodyInserters.empty(), responseBodyType, options);
    }

    @Override
    public <T> Mono<T> post(
            Function<UriBuilder, UriBuilder> uriBuilder,
            BodyInserter<?, ? super ClientHttpRequest> requestBody,
            ParameterizedTypeReference<T> responseBodyType,
            RequestOptions options
    ){
        return retrieve(HttpMethod.POST, uriBuilder, requestBody, responseBodyType, options);
    }

    @Override
    public <T> Mono<T> put(
            Function<UriBuilder, UriBuilder> uriBuilder,
            BodyInserter<?, ? super ClientHttpRequest> requestBody,
            ParameterizedTypeReference<T> responseBodyType,
            RequestOptions options
    ){
        return retrieve(HttpMethod.PUT, uriBuilder, requestBody, responseBodyType, options);
    }

    @Override
    public <T> Mono<T> retrieve(
            HttpMethod method,
            Function<UriBuilder, UriBuilder> uriBuilder,
            BodyInserter<?, ? super ClientHttpRequest> requestBody,
            ParameterizedTypeReference<T> responseBodyType,
            RequestOptions options
    ){
        return wrapException(
                httpRetrieve(method, uriBuilder, requestBody, options)
                    .bodyToMono(responseBodyType)
        );
    }

    /***************************************************************************
     *                                                                         *
     * Protected Http retrieve API                                             *
     *                                                                         *
     **************************************************************************/


    @Override
    public <T> Mono<T> delete(
            Function<UriBuilder, UriBuilder> uriBuilder,
            Class<T> responseBodyType,
            RequestOptions options
    ){
        return retrieve(HttpMethod.DELETE, uriBuilder, BodyInserters.empty(), responseBodyType, options);
    }

    @Override
    public <T> Mono<T> get(
            Function<UriBuilder, UriBuilder> uriBuilder,
            Class<T> responseBodyType
    ){
        return retrieve(HttpMethod.GET, uriBuilder, BodyInserters.empty(), responseBodyType, RequestOptions.empty());
    }

    @Override
    public <T> Mono<T> get(
            Function<UriBuilder, UriBuilder> uriBuilder,
            Class<T> responseBodyType,
            RequestOptions options
    ){
        return retrieve(HttpMethod.GET, uriBuilder, BodyInserters.empty(), responseBodyType, options);
    }

    @Override
    public <T> Mono<T> post(
            Function<UriBuilder, UriBuilder> uriBuilder,
            BodyInserter<?, ? super ClientHttpRequest> requestBody,
            Class<T> responseBodyType,
            RequestOptions options
    ){
        return retrieve(HttpMethod.POST, uriBuilder, requestBody, responseBodyType, options);
    }

    @Override
    public <T> Mono<T> put(
            Function<UriBuilder, UriBuilder> uriBuilder,
            BodyInserter<?, ? super ClientHttpRequest> requestBody,
            Class<T> responseBodyType,
            RequestOptions options
    ){
        return retrieve(HttpMethod.PUT, uriBuilder, requestBody, responseBodyType, options);
    }

    @Override
    public <T> Mono<T> retrieve(
            HttpMethod method,
            Function<UriBuilder, UriBuilder> uriBuilder,
            BodyInserter<?, ? super ClientHttpRequest> requestBody,
            Class<T> responseBodyType,
            RequestOptions options
    ){
        return wrapException(
                httpRetrieve(method, uriBuilder, requestBody, options)
                    .bodyToMono(responseBodyType)
        );
    }

    /***************************************************************************
     *                                                                         *
     * Protected Http Request builder API                                      *
     *                                                                         *
     **************************************************************************/

    private <T> Mono<T> wrapException(Mono<T> mono){
        return mono.onErrorMap(err -> {
            if(err instanceof WebClientResponseException){
                return adapt((WebClientResponseException)err);
            }else{
                return err;
            }
        });
    }

    public static HttpResponseError adapt(WebClientResponseException err){
        if(err.getStatusCode().is4xxClientError()){
            return new HttpClientResponseError(
                    err.getRequest(),
                    err.getStatusCode(),
                    err.getStatusText(),
                    err.getResponseBodyAsString()
            );
        }else if(err.getStatusCode().is5xxServerError()){
            return new HttpServerResponseError(
                    err.getRequest(),
                    err.getStatusCode(),
                    err.getStatusText(),
                    err.getResponseBodyAsString()
            );
        }else{
            return new HttpResponseError(
                    err.getRequest(),
                    err.getStatusCode(),
                    err.getStatusText(),
                    err.getResponseBodyAsString()
            );
        }
    }

    protected WebClient.ResponseSpec httpRetrieve(
            HttpMethod method,
            Function<UriBuilder, UriBuilder> uriBuilder,
            BodyInserter<?, ? super ClientHttpRequest> requestBody,
            RequestOptions options
    ){
        return http(method, uriBuilder, requestBody, options)
                .retrieve();
    }

    protected WebClient.RequestHeadersSpec http(
                HttpMethod method,
                Function<UriBuilder, UriBuilder> uriBuilder,
                BodyInserter<?, ? super ClientHttpRequest> requestBody,
                RequestOptions options
    ){
            return client.method(method)
                    .uri(b -> uriBuilder.apply(b)
                            .queryParams(options.getQueryParams())
                            .build()
                    ).body(requestBody)
                    .headers(h -> h.addAll(options.getHeaders()));
    }
}
