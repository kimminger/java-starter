package com.elderbyte.commons.cancelation;

import java.util.concurrent.CancellationException;

public interface CancellationToken {

    /**
     * Gets a CancellationToken which will never be cancelled.
     */
    CancellationToken Never = new CancellationTokenImpl.CancellationTokenNeverImpl();

    /**
     * @return {@code true} if the cancellation was requested from the source, {@code false} otherwise.
     */
    boolean isCancellationRequested();

    /**
     * @throws CancellationException if this token has had cancellation requested.
     * May be used to stop execution of a thread or runnable.
     */
    void throwIfCancellationRequested() throws CancellationException;
}
