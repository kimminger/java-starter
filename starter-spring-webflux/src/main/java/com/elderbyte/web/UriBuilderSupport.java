package com.elderbyte.web;

import com.elderbyte.commons.data.contiunation.ContinuationToken;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.util.UriBuilder;

public class UriBuilderSupport {

    public static UriBuilder apply(UriBuilder uriBuilder, Pageable pageable){

        if(pageable == null) return uriBuilder;

        if(pageable.isPaged()){
            uriBuilder
                    .queryParam("page", pageable.getPageNumber())
                    .queryParam("size", pageable.getPageSize());
        }

        return apply(uriBuilder, pageable.getSort());
    }

    public static UriBuilder apply(UriBuilder uriBuilder, Sort sort, ContinuationToken token){
        return apply(apply(uriBuilder, sort), token);

    }

    public static UriBuilder apply(UriBuilder uriBuilder, ContinuationToken token){

        if(token == null) return uriBuilder;

        return token.getTokenIfNotEmpty().map(
                t -> uriBuilder
                        .queryParam("continuationToken", t)
        ).orElse(uriBuilder);
    }

    public static UriBuilder apply(UriBuilder uriBuilder, Sort sort){

        if(sort == null) return uriBuilder;

        return sort.isSorted()
                ? uriBuilder.queryParam("sort", sort.toString())
                : uriBuilder;
    }

}
