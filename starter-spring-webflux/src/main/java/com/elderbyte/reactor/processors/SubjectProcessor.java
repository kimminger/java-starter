package com.elderbyte.reactor.processors;

import reactor.core.publisher.DirectProcessor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxProcessor;

/**
 * Builder to create subject processors.
 *
 * A subject processor dispatches all events to all subscribers.
 * Unlike the default processors, the subject processor simply drops messages when
 * there are no subscribers or when there is too high back pressure.
 *
 */
public class SubjectProcessor<T> {

    /***************************************************************************
     *                                                                         *
     * Static builder                                                          *
     *                                                                         *
     **************************************************************************/

    public static <T> SubjectProcessor<T> build(){
        return build(DirectProcessor.create());
    }

    public static <T> SubjectProcessor<T> build(FluxProcessor<T, T> processor){
        return new SubjectProcessor<>(processor);
    }

    /***************************************************************************
     *                                                                         *
     * Fields                                                                  *
     *                                                                         *
     **************************************************************************/

    private final FluxProcessor<T, T> subjectProcessor;

    /***************************************************************************
     *                                                                         *
     * Constructor                                                             *
     *                                                                         *
     **************************************************************************/

    protected SubjectProcessor(FluxProcessor<T, T> processor){
        this.subjectProcessor = FluxProcessor.wrap(
                processor,
                processor.onBackpressureDrop()
        );
    }

    /***************************************************************************
     *                                                                         *
     * Properties                                                              *
     *                                                                         *
     **************************************************************************/

    public Flux<T> getFlux(){
        return subjectProcessor;
    }

    /***************************************************************************
     *                                                                         *
     * Public API                                                              *
     *                                                                         *
     **************************************************************************/

    /**
     * Emit a event
     */
    public void onNext(T o){
        subjectProcessor.onNext(o);
    }

    /**
     * Gets the underlying flux processor
     */
    public FluxProcessor<T, T> unwrap(){
        return subjectProcessor;
    }
}
