package com.elderbyte.spring.cloud.bootstrap.support;

import com.elderbyte.commons.data.contiunation.JsonContinuationToken;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.Optional;

public class PageableContinuationToken extends JsonContinuationToken
{
    /**
     * Encodes the given pageable into a continuation-token.
     */
    public static PageableContinuationToken from(String raw){
        return new PageableContinuationToken(raw);
    }


    /**
     * Encodes the given pageable into a continuation-token.
     */
    public static PageableContinuationToken fromPageable(Pageable pageable){
        return new PageableContinuationToken(new PageableDto(pageable));
    }

    private PageableContinuationToken(String token) {
        super(token);
    }

    private PageableContinuationToken(PageableDto pageable) {
        super(pageable);
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
        return asJson(PageableDto.class)
                .map(p -> PageRequest.of(p.pageIndex, p.pageSize, sort));
    }
}
