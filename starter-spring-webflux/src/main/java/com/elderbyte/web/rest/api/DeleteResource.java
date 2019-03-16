package com.elderbyte.web.rest.api;

import reactor.core.publisher.Mono;

public interface DeleteResource<TId> {
    Mono<Void> delete(TId id);
}
