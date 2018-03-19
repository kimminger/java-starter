package com.elderbyte.commons.data.contiunation;

import com.elderbyte.commons.utils.Utf8Base64;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Optional;

/**
 * Represents a continuation token which supports payload data:
 *
 * token := base64(json-data)
 */
public class JsonContinuationToken {

    private static ObjectMapper mapper = new ObjectMapper();

    /**
     * Constructs a continuation-token from the given raw string
     */
    public static JsonContinuationToken from(ContinuationToken token){
        return new JsonContinuationToken(token);
    }

    /**
     * Constructs a continuation-token from the given raw string
     */
    public static ContinuationToken buildToken(Object payload){
        return buildJsonToken(payload);
    }


    private final ContinuationToken token;

    protected JsonContinuationToken(ContinuationToken token) {
        this.token = token;
    }

    public ContinuationToken getToken(){
        return this.token;
    }

    public Optional<String> decodeBase64(){
        return token.getTokenIfNotEmpty()
                .map(token -> Utf8Base64.decodeUtf8(token));
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
    private static ContinuationToken buildJsonToken(Object data){
        try {
            String json = mapper.writeValueAsString(data);
            String base64 = Utf8Base64.encodeUtf8(json);
            return ContinuationToken.from(base64);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("Could encode object as json string!", e);
        }
    }
}
