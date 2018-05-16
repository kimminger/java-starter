package com.elderbyte.commons.data.contiunation;

import com.elderbyte.commons.exceptions.ArgumentNullException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Optional;

/**
 * Represents a continuation token which supports payload data:
 *
 * token := base64(json-data)
 */
public class JsonContinuationToken {

    /***************************************************************************
     *                                                                         *
     * Static Builders                                                         *
     *                                                                         *
     **************************************************************************/

    private static JsonContinuationTokenBuilder builderInstance = new JsonContinuationTokenBuilder(new ObjectMapper());

    public static void setBuilder(JsonContinuationTokenBuilder builder){
        if(builder == null) throw new ArgumentNullException("builder");
        builderInstance = builder;
    }


    /**
     * Constructs a continuation-token from the given raw string
     */
    public static JsonContinuationToken from(ContinuationToken token){
        return new JsonContinuationToken(builderInstance, token);
    }

    /**
     * Constructs a continuation-token from the given raw string
     */
    public static ContinuationToken buildToken(Object payload){
        return builderInstance.buildJsonToken(payload);
    }

    /***************************************************************************
     *                                                                         *
     * Fields                                                                  *
     *                                                                         *
     **************************************************************************/

    private final JsonContinuationTokenBuilder builder;
    private final ContinuationToken token;

    /***************************************************************************
     *                                                                         *
     * Constructors                                                            *
     *                                                                         *
     **************************************************************************/

    protected JsonContinuationToken(JsonContinuationTokenBuilder builder, ContinuationToken token) {

        if(builder == null) throw new ArgumentNullException("builder");
        if(token == null) throw new ArgumentNullException("token");

        this.builder = builder;
        this.token = token;
    }

    /***************************************************************************
     *                                                                         *
     * Properties                                                              *
     *                                                                         *
     **************************************************************************/

    public ContinuationToken getToken(){
        return this.token;
    }

    public <T> Optional<T> asJson(Class<T> clazz){
        return builder.asJson(token, clazz);
    }

}
