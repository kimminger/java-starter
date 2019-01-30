package com.elderbyte.spring.boot.bootstrap.data.continuation;

import com.elderbyte.commons.data.contiunation.ContinuationToken;
import com.elderbyte.commons.data.contiunation.JsonContinuationToken;
import com.elderbyte.spring.boot.bootstrap.data.pagination.PageableDto;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.Optional;

public class PageableContinuationToken {

    /**
     * Encodes the given pageable into a continuation-token.
     */
    public static PageableContinuationToken from(ContinuationToken token){
        return new PageableContinuationToken(token);
    }

    /**
     * Encodes the given pageable into a continuation-token.
     */
    public static ContinuationToken buildToken(Pageable pageable){
        return new PageableContinuationToken(new PageableDto(pageable)).getToken();
    }

    private final JsonContinuationToken token;

    private PageableContinuationToken(ContinuationToken token) {
        this.token = JsonContinuationToken.from(token);
    }

    private PageableContinuationToken(PageableDto pageable) {
        this(JsonContinuationToken.buildToken(pageable));
    }


    public ContinuationToken getToken(){
        return this.token.getToken();
    }

    /**
     * Decodes a continuation-token into an unsorted pageable
     */
    public Optional<Pageable> asPageable(){
        return asPageable(Sort.unsorted());
    }

    /**
     * Decodes a continuation-token into a pageable
     * @param sort The sort to use
     */
    public Optional<Pageable> asPageable(Sort sort){
        return token.asJson(PageableDto.class)
                .map(p -> PageRequest.of(p.pageIndex, p.pageSize, sort));
    }
}
