package com.elderbyte.web.rest.api;

import reactor.core.publisher.Mono;

public interface GetResource<TBody, TId> {

    Mono<TBody> getById(TId id);

}
