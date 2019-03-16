package com.elderbyte.web.client;

import org.springframework.http.HttpHeaders;
import org.springframework.util.MultiValueMap;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public interface RequestOptions {

    static RequestOptions empty() {
        return start();
    }

    static RequestOptions start() {
        return new RequestOptionsImpl();
    }

    /***************************************************************************
     *                                                                         *
     * Properties                                                              *
     *                                                                         *
     **************************************************************************/

    HttpHeaders getHeaders();

    MultiValueMap<String, String> getQueryParams();

    /***************************************************************************
     *                                                                         *
     * Builder                                                                 *
     *                                                                         *
     **************************************************************************/

    RequestOptions headers(Function<HttpHeaders, HttpHeaders> headers);

    RequestOptions param(String key, String value);

    RequestOptions params(String key, Collection<String> values);

    RequestOptions paramsConvert(String key, Collection<?> values);

    RequestOptions merge(MultiValueMap<String, String> params);

    RequestOptions appendParam(String key, String value);

    /**
     * Apply all properties (getters) from the given bean
     * as query merge.
     */
    RequestOptions withBean(Object bean);

    /***************************************************************************
     *                                                                         *
     * Read API                                                                *
     *                                                                         *
     **************************************************************************/

    Optional<String> getParam(String key);

    Optional<Boolean> getParamBool(String key);

    Optional<Integer> getParamInt(String key);

    Optional<Long> getParamLong(String key);

    Optional<Double> getParamDouble(String key);

    List<String> getParams(String key);
}
