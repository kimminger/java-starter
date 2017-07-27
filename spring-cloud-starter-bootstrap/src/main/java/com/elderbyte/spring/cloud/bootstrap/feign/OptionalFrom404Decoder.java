package com.elderbyte.spring.cloud.bootstrap.feign;

import feign.Response;
import feign.Util;
import feign.codec.Decoder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;

import java.io.DataInputStream;
import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Optional;

/**
 * Decodes 404 Responses into empty optional.
 * If no Optional is defined, throws a HttpClientErrorException.
 */
public class OptionalFrom404Decoder implements Decoder {

    private final Decoder delegate;

    public OptionalFrom404Decoder(Decoder delegate){
        if(delegate == null) throw new IllegalArgumentException("delegate was null!");
        this.delegate = delegate;
    }

    @Override
    public Object decode(Response response, Type type) throws IOException {
        if(isOptional(type)){
            if (response.status() == 404){
                return Optional.empty();
            }else{
                Type enclosedType = Util.resolveLastTypeParameter(type, Optional.class);
                return Optional.of(delegate.decode(response, enclosedType));
            }
        }else{
           if(response.status() == 404){
               throw buildClientHttpError(response);
           }
        }
        return delegate.decode(response, type);
    }

    private HttpClientErrorException buildClientHttpError(Response response){
        String statusText = response.reason();

        HttpHeaders responseHeaders = new HttpHeaders();
        response.headers().entrySet()
                .forEach(entry -> responseHeaders.put(entry.getKey(), new ArrayList<>(entry.getValue())));

        byte[] responseBody;
        try {
            responseBody = new byte[response.body().length()];
            new DataInputStream(response.body().asInputStream()).readFully(responseBody);
            if(statusText == null){
                statusText = new String(responseBody);
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to process response body.", e);
        }

        return new HttpClientErrorException(HttpStatus.NOT_FOUND, statusText, responseHeaders, responseBody, null);
    }

    static boolean isOptional(Type type) {
        if (!(type instanceof ParameterizedType)) return false;
        ParameterizedType parameterizedType = (ParameterizedType) type;
        return parameterizedType.getRawType().equals(Optional.class);
    }
}