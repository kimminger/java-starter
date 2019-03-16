package com.elderbyte.web.rest.api;

import reactor.core.publisher.Mono;

public interface UpdateResource<TBody, TId> {
    Mono<TBody> update(TId id, TBody entity);
}
