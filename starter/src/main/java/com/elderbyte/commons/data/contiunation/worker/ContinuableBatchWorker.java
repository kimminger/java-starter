package com.elderbyte.commons.data.contiunation.worker;

import com.elderbyte.commons.cancelation.CancellationToken;
import com.elderbyte.commons.data.contiunation.ContinuableListing;
import com.elderbyte.commons.exceptions.ArgumentNullException;

import java.util.List;
import java.util.concurrent.CancellationException;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Provides a simple way to work over a continuable stream of batches.
 *
 * 1. Loads the next batch
 * 2. Processes the batch
 * 3. Go to 1, until no more batches
 *
 * This worker will collect some basic metrics and report them back.
 *
 * @param <T>
 */
public class ContinuableBatchWorker<T> {

    /***************************************************************************
     *                                                                         *
     * Static builders                                                         *
     *                                                                         *
     **************************************************************************/

    /**
     * Creates a new batch worker
     * @param batchLoader Function which loads a continuable list of data by a continuation token
     * @param batchProcessor A processing function for a loaded chunk of data
     */
    public static <G> ContinuableBatchWorker<G> worker(
            Function<String, ContinuableListing<G>> batchLoader,
            Consumer<List<G>> batchProcessor){
        return new ContinuableBatchWorker<>(batchLoader, (listing) -> batchProcessor.accept(listing.getContent()));
    }

    /**
     * Creates a new batch worker
     * @param batchLoader Function which loads a continuable list of data by a continuation token
     * @param batchProcessor A processing function for a loaded chunk of data, including the current state token
     */
    public static <G> ContinuableBatchWorker<G> workerWithListing(
            Function<String, ContinuableListing<G>> batchLoader,
            Consumer<ContinuableListing<G>> batchProcessor){
        return new ContinuableBatchWorker<>(batchLoader, batchProcessor);
    }

    /***************************************************************************
     *                                                                         *
     * Fields                                                                  *
     *                                                                         *
     **************************************************************************/

    private final Function<String, ContinuableListing<T>> batchLoader;
    private final Consumer<ContinuableListing<T>> batchProcessor;

    /***************************************************************************
     *                                                                         *
     * Constructor                                                             *
     *                                                                         *
     **************************************************************************/

    private ContinuableBatchWorker(
            Function<String, ContinuableListing<T>> batchLoader,
            Consumer<ContinuableListing<T>> batchProcessor
    ){
        if(batchLoader == null) throw new ArgumentNullException("batchLoader");
        if(batchProcessor == null) throw new ArgumentNullException("batchProcessor");

        this.batchLoader = batchLoader;
        this.batchProcessor = batchProcessor;
    }

    /***************************************************************************
     *                                                                         *
     * Public API                                                              *
     *                                                                         *
     **************************************************************************/

    /**
     * Process all items from the continuable source.
     * @throws BatchWorkerException Thrown when there was an issue processing a batch.
     */
    public void processAll() throws BatchWorkerException {
        processAll(metrics -> {});
    }

    /**
     * Process all items from the continuable source.
     * @param progressCallback Callback for metrics report while the worker is processing
     * @throws BatchWorkerException Thrown when there was an issue processing a batch.
     */
    public void processAll(Consumer<WorkerBatchMetricRecord> progressCallback) throws BatchWorkerException {
        processAll(progressCallback, CancellationToken.Never);
    }

    /**
     * Process all items from the continuable source.
     * @param progressCallback Callback for metrics report while the worker is processing
     * @throws BatchWorkerException Thrown when there was an issue processing a batch.
     */
    public void processAll(Consumer<WorkerBatchMetricRecord> progressCallback,
                           CancellationToken cancellationToken) throws BatchWorkerException {
        processAllFrom(null, progressCallback, cancellationToken);
    }

    /**
     * Process all items from the continuable source.
     * @param startToken The initial token to use - useful to manually resume work.
     * @throws BatchWorkerException
     */
    public void processAllFrom(String startToken) throws BatchWorkerException {
        processAllFrom(startToken, metrics -> {}, CancellationToken.Never);
    }

    /**
     * Process all items from the continuable source starting at the given token.
     * @param startToken The initial token to use - useful to manually resume work. If null starts from the beginning.
     * @param progressCallback Callback for metrics report while the worker is processing
     * @param cancellationToken Token to cancel processing - has to be provided.
     * @throws BatchWorkerException Thrown when there was an issue processing a batch.
     */
    public void processAllFrom(
            String startToken,
            Consumer<WorkerBatchMetricRecord> progressCallback,
            CancellationToken cancellationToken) throws BatchWorkerException, CancellationException {

        if(progressCallback == null) throw new ArgumentNullException("progressCallback");
        if(cancellationToken == null) throw new ArgumentNullException("cancellationToken");


        String nextToken = startToken;

        do {

            // Check if processing should be aborted
            cancellationToken.throwIfCancellationRequested();

            long startLoading = System.nanoTime();

            var chunk = loadNext(nextToken);

            long loadingTime = System.nanoTime()-startLoading;

            long startProcessing = System.nanoTime();

            processAll(chunk);

            nextToken = chunk.getNextContinuationToken();

            var processingTime = System.nanoTime()-startProcessing;

            progressCallback.accept(
                    WorkerBatchMetricRecord.fromListing(
                            chunk,
                            loadingTime,
                            processingTime
                    )
            );

        } while (nextToken != null);
    }

    /***************************************************************************
     *                                                                         *
     * Private API                                                             *
     *                                                                         *
     **************************************************************************/

    private ContinuableListing<T> loadNext(String nextToken){
        try {
            return batchLoader.apply(nextToken);
        }catch (Exception e){
            throw new BatchWorkerException("Failed to load next batch with nextToken: " + nextToken, e);
        }
    }

    private void processAll(ContinuableListing<T> batchListing){
        try {
            batchProcessor.accept(batchListing);
        }catch (Exception e){
            throw new BatchWorkerException("Failed to process item batch!", e);
        }
    }

}
