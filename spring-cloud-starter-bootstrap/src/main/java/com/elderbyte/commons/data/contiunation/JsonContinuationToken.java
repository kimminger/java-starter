package com.elderbyte.commons.data.contiunation;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Base64;
import java.util.Optional;

/**
 * Represents a continuation token which supports payload data:
 *
 * token := base64(json-data)
 */
public class JsonContinuationToken extends SimpleContinuationToken {

    private static ObjectMapper mapper = new ObjectMapper();

    /**
     * Constructs a continuation-token from the given raw string
     */
    public static JsonContinuationToken from(String token){
        return new JsonContinuationToken(token);
    }

    /**
     * Constructs a continuation-token from the given raw string
     */
    public static JsonContinuationToken build(Object payload){
        return new JsonContinuationToken(payload);
    }



    protected JsonContinuationToken(String token) {
        super(token);
    }

    protected JsonContinuationToken(Object data) {
        super(buildToken(data));
    }

    public Optional<String> decodeBase64(){
        return getTokenIfNotEmpty()
                .map(token -> {
                    try {
                        byte[] data = Base64.getDecoder().decode(token);
                        return new String(data, "UTF8");
                    }catch (Exception e){
                        throw new IllegalStateException("Could not decode token as base64 utf8 string!", e);
                    }
                });
    }

    public <T> Optional<T> asJson(Class<T> clazz){
        return decodeBase64()
                .map(token -> {
                    try {
                        return mapper.readValue(token, clazz);
                    } catch (IOException e) {
                        throw new IllegalStateException("Could not parse token as JSON", e);
                    }
                });
    }


    /**
     * Builds a continuation token with the given payload
     */
    private static String buildToken(Object data){
        try {
            String json = mapper.writeValueAsString(data);
            byte[] binaryJson = json.getBytes("UTF8");
            String base64 = Base64.getEncoder().encodeToString(binaryJson);
            return base64;
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("Could encode object as json string!", e);
        } catch (UnsupportedEncodingException e) {
            throw new IllegalStateException("UTF8 should be supported!", e);
        }
    }
}
