package com.elderbyte.web.rest;

import com.elderbyte.web.client.WebClientApi;
import com.elderbyte.web.client.RequestOptions;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Mono;

public class RestEndpointPaged<TBody, TId, TCreate, TFilter>
        extends RestEndpoint<TBody, TId, TCreate> {


    /***************************************************************************
     *                                                                         *
     * Fields                                                                  *
     *                                                                         *
     **************************************************************************/

    /***************************************************************************
     *                                                                         *
     * Constructor                                                             *
     *                                                                         *
     **************************************************************************/

    public RestEndpointPaged(
            WebClientApi client,
            String endpoint,
            Class<TBody> resourceType) {
        super(client, endpoint, resourceType);
    }

    /***************************************************************************
     *                                                                         *
     * Public API                                                              *
     *                                                                         *
     **************************************************************************/

    public Mono<Page<TBody>> listAllPaged(TFilter filter, Pageable pageable){

        return listAllPaged(
                RequestOptions.start()
                        .withBean(filter),
                pageable
        );
    }
}
