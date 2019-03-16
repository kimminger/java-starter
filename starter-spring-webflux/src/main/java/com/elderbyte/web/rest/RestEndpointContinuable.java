package com.elderbyte.web.rest;

import com.elderbyte.commons.data.contiunation.ContinuableListing;
import com.elderbyte.commons.data.contiunation.ContinuationToken;
import com.elderbyte.web.client.WebClientApi;
import com.elderbyte.web.client.RequestOptions;
import com.elderbyte.web.rest.api.ContinuableResource;
import org.springframework.data.domain.Sort;
import reactor.core.publisher.Mono;

public class RestEndpointContinuable<TBody, TId, TCreate, TFilter>
        extends RestEndpoint<TBody, TId, TCreate> implements ContinuableResource<TBody, TFilter> {

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

    public RestEndpointContinuable(
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

    public Mono<ContinuableListing<TBody>> listAllContinuable(){
        return listAllContinuable(
                RequestOptions.empty(),
                ContinuationToken.Empty,
                Sort.unsorted()
        );
    }

    @Override
    public Mono<ContinuableListing<TBody>> listAllContinuable(TFilter filter){
        return listAllContinuable(
                filter,
                ContinuationToken.Empty
        );
    }


    @Override
    public Mono<ContinuableListing<TBody>> listAllContinuable(TFilter filter,
                                                              ContinuationToken token){
        return listAllContinuable(
                filter,
                token,
                Sort.unsorted()
        );
    }

    @Override
    public Mono<ContinuableListing<TBody>> listAllContinuable(
            TFilter filter,
            ContinuationToken token,
            Sort sort){

        return listAllContinuable(
                RequestOptions.start()
                        .withBean(filter),
                token,
                sort
        );
    }

    /***************************************************************************
     *                                                                         *
     * Private methods                                                         *
     *                                                                         *
     **************************************************************************/



}
