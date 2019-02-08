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

    private Consumer<ProcessingMetric> processingCb = null;
    private Consumer<LoadingMetric> loadingCb = null;

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

    public ContinuableBatchWorker<T> loadingMetrics(Consumer<LoadingMetric> loadingCb){
        this.loadingCb = loadingCb;
        return this;
    }

    public ContinuableBatchWorker<T> processingMetrics(Consumer<ProcessingMetric> processingCb){
        this.processingCb = processingCb;
        return this;
    }


    /**
     * Process all items from the continuable source.
     * @throws BatchWorkerException Thrown when there was an issue processing a batch.
     */
    public void processAll() throws BatchWorkerException {
        processAll(CancellationToken.Never);
    }

    /**
     * Process all items from the continuable source.
     * @throws BatchWorkerException Thrown when there was an issue processing a batch.
     */
    public void processAll(
                           CancellationToken cancellationToken) throws BatchWorkerException {
        processAllFrom(null, cancellationToken);
    }

    /**
     * Process all items from the continuable source.
     * @param startToken The initial token to use - useful to manually resume work.
     * @throws BatchWorkerException
     */
    public void processAllFrom(String startToken) throws BatchWorkerException {
        processAllFrom(startToken, CancellationToken.Never);
    }

    /**
     * Process all items from the continuable source starting at the given token.
     * @param startToken The initial token to use - useful to manually resume work. If null starts from the beginning.
     * @param cancellationToken Token to cancel processing - has to be provided.
     * @throws BatchWorkerException Thrown when there was an issue processing a batch.
     */
    public void processAllFrom(
            String startToken,
            CancellationToken cancellationToken) throws BatchWorkerException, CancellationException {

        if(cancellationToken == null) throw new ArgumentNullException("cancellationToken");

        String nextToken = startToken;

        do {
            // Check if processing should be aborted
            cancellationToken.throwIfCancellationRequested();

            var chunk = loadNext(nextToken);

            nextToken = processAll(chunk);

        } while (nextToken != null);
    }

    /***************************************************************************
     *                                                                         *
     * Private API                                                             *
     *                                                                         *
     **************************************************************************/


    private ContinuableListing<T> loadNext(String nextToken){

        long startLoading = System.nanoTime();

        try {

            var listing = batchLoader.apply(nextToken);
            if(listing == null){
                throw new IllegalStateException("The loaded listing was null which is illegal!");
            }

            long loadingTime = System.nanoTime()-startLoading;

            record(LoadingMetric.fromTime(loadingTime));

            return listing;

        }catch (Exception e){
            record(LoadingMetric.error(e, System.nanoTime()-startLoading));
            throw new BatchWorkerException("Failed to load next batch with nextToken: " + nextToken, e);
        }
    }

    private String processAll(ContinuableListing<T> listing){

        long startProcessing = System.nanoTime();

        try {
            if(listing == null) throw new IllegalStateException("The listing to process was null which is illegal!");

            batchProcessor.accept(listing);

            var processingTime = System.nanoTime()-startProcessing;

            record(
                    ProcessingMetric.fromListing(
                            listing,
                            processingTime
                    )
            );

            return listing.getNextContinuationToken();

        }catch (Exception e){
            record(ProcessingMetric.error(e, listing,System.nanoTime()-startProcessing));
            throw new BatchWorkerException("Failed to process batch listing!", e);
        }
    }

    private void record(LoadingMetric metric){
        var instrument = loadingCb;
        if(instrument != null){
            instrument.accept(
                    metric
            );
        }
    }

    private void record(ProcessingMetric metric){
        var instrument = processingCb;
        if (instrument != null) {
            instrument.accept(metric);
        }
    }

}
