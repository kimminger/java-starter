package com.elderbyte.commons.data.contiunation;

public interface ContinuationToken {

    static ContinuationToken from(String token){
        return new SimpleContinuationToken(token);
    }

    String getToken();
}
