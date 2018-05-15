package com.elderbyte.commons.cancelation;

import java.util.concurrent.CancellationException;

/**
 * A cancellation sonde which will inform async code when it should prematurely stop execution.
 * Use the {@link CancellationTokenSource} to create a new cancellation context and tokens.
 */
public interface CancellationToken {

    /**
     * Gets a CancellationToken which will never be cancelled.
     * Use this instead of a null token.
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
