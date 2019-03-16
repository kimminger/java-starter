package com.elderbyte.web.rest.api;

import com.elderbyte.commons.data.contiunation.ContinuableListing;
import com.elderbyte.commons.data.contiunation.ContinuationToken;
import org.springframework.data.domain.Sort;
import reactor.core.publisher.Mono;

public interface ContinuableResource<TBody, TFilter> {

    Mono<ContinuableListing<TBody>> listAllContinuable(TFilter filter);

    Mono<ContinuableListing<TBody>> listAllContinuable(TFilter filter,
                                                       ContinuationToken token);

    Mono<ContinuableListing<TBody>> listAllContinuable(
            TFilter filter,
            ContinuationToken token,
            Sort sort);
}
