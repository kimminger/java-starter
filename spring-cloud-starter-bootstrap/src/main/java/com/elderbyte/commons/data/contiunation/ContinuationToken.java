package com.elderbyte.commons.data.contiunation;

import com.elderbyte.commons.exceptions.ArgumentNullException;

import java.util.Optional;

public interface ContinuationToken {

    ContinuationToken Empty = SimpleContinuationToken.Empty;

    static ContinuationToken from(String token){
        if(token == null) throw new ArgumentNullException("token");

        return new SimpleContinuationToken(token);
    }

    /**
     * Gets the raw token value - might be empty
     */
    String getToken();

    default Optional<String> getTokenIfNotEmpty(){
        String token = getToken();
        return !token.isEmpty() ? Optional.of(token) : Optional.empty();
    }
}
