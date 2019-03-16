package com.elderbyte.web.client;

import com.elderbyte.web.QueryMapUtil;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.*;
import java.util.function.Function;

/**
 * Represents additional parameters which can be sent with a request.
 * These are query parameters and headers.
 */
public class RequestOptionsImpl implements RequestOptions {

    /***************************************************************************
     *                                                                         *
     * Fields                                                                  *
     *                                                                         *
     **************************************************************************/

    private final MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
    private final HttpHeaders headers = new HttpHeaders();

    /***************************************************************************
     *                                                                         *
     * Constructor                                                             *
     *                                                                         *
     **************************************************************************/

    /**
     * Creates a new RequestOptionsImpl
     */
    public RequestOptionsImpl() {

    }

    public RequestOptionsImpl(ServerHttpRequest request){
        this(request.getQueryParams(), request.getHeaders());
    }

    /**
     * Creates a new RequestOptionsImpl
     */
    public RequestOptionsImpl(
            MultiValueMap<String, String> queryParams,
            HttpHeaders headers
    ) {
        this.queryParams.addAll(queryParams);
        this.headers.addAll(headers);
    }

    /***************************************************************************
     *                                                                         *
     * Properties                                                              *
     *                                                                         *
     **************************************************************************/

    public HttpHeaders getHeaders() {
        return headers;
    }

    public MultiValueMap<String, String> getQueryParams() {
        return queryParams;
    }

    /***************************************************************************
     *                                                                         *
     * Public API                                                              *
     *                                                                         *
     **************************************************************************/

    public RequestOptions headers(Function<HttpHeaders, HttpHeaders> headers){
        headers.apply(this.headers);
        return this;
    }

    public RequestOptions param(String key, String value){
        queryParams.set(key, value);
        return this;
    }

    public RequestOptions params(String key, Collection<String> values){
        values.stream()
                .filter(Objects::nonNull)
                .forEach(v -> appendParam(key, v));
        return this;
    }

    public RequestOptions paramsConvert(String key, Collection<?> values){
        values.stream()
                .filter(Objects::nonNull)
                .forEach(v -> appendParam(key, v.toString()));
        return this;
    }

    public RequestOptions appendParam(String key, String value){
        queryParams.add(key, value);
        return this;
    }

    public RequestOptions merge(MultiValueMap<String, String> params){
        queryParams.addAll(params);
        return this;
    }


    @Override
    public RequestOptions withBean(Object bean) {
        return merge(QueryMapUtil.toQueryMap(bean));
    }

    public Optional<String> getParam(String key){
        return Optional.ofNullable(queryParams.getFirst(key));
    }

    public Optional<Boolean> getParamBool(String key){
        return getParam(key).map(Boolean::parseBoolean);
    }

    public Optional<Integer> getParamInt(String key){
        return getParam(key).map(Integer::parseInt);
    }

    public Optional<Long> getParamLong(String key){
        return getParam(key).map(Long::parseLong);
    }

    public Optional<Double> getParamDouble(String key){
        return getParam(key).map(Double::parseDouble);
    }

    public List<String> getParams(String key){
        var values = queryParams.get(key);
        if(values == null){
            values = Collections.emptyList();
        }
        return values;
    }

}
