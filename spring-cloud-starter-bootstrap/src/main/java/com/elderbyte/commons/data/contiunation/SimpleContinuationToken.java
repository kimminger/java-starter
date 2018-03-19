package com.elderbyte.commons.data.contiunation;

import com.elderbyte.commons.exceptions.ArgumentNullException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.Objects;
import java.util.Optional;

public class SimpleContinuationToken implements ContinuationToken {

    private final String token;
    public static final SimpleContinuationToken Empty = new SimpleContinuationToken("");

    SimpleContinuationToken(String token){
        if(token == null) throw new ArgumentNullException("token");
        this.token = token;
    }

    @Override
    public String getToken() {
        return token;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SimpleContinuationToken that = (SimpleContinuationToken) o;
        return Objects.equals(token, that.token);
    }

    @Override
    public int hashCode() {
        return Objects.hash(token);
    }
}
