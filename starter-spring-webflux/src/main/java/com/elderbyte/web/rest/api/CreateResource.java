package com.elderbyte.web.rest.api;

import reactor.core.publisher.Mono;

public interface CreateResource<TBody, TCreate> {
    Mono<TBody> create(TCreate newEntity);
}
