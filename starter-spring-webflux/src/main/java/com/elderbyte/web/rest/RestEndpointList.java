package com.elderbyte.web.rest;

import com.elderbyte.web.client.WebClientApi;
import com.elderbyte.web.client.RequestOptions;
import org.springframework.data.domain.Sort;
import reactor.core.publisher.Mono;

import java.util.List;

public class RestEndpointList<TBody, TId, TCreate, TFilter>
        extends RestEndpoint<TBody, TId, TCreate> {

    /***************************************************************************
     *                                                                         *
     * Constructor                                                             *
     *                                                                         *
     **************************************************************************/

    /**
     * Creates a new RestEndpointList
     */
    public RestEndpointList(
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

    public Mono<List<TBody>> listAll(){
        return listAll(Sort.unsorted());
    }

    public Mono<List<TBody>> listAll(Sort sort){
        return listAll(RequestOptions.empty(), sort);
    }

    public Mono<List<TBody>> listAll(TFilter filter){
        return listAll(filter, Sort.unsorted());
    }

    public Mono<List<TBody>> listAll(TFilter filter, Sort sort){
        return listAll(RequestOptions.start()
                                    .withBean(filter),
                sort
        );
    }
}
