package com.elderbyte.commons.data.contiunation.worker;

import com.elderbyte.commons.data.contiunation.ContinuableListing;
import com.elderbyte.commons.exceptions.ArgumentNullException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
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

    public static <G> ContinuableBatchWorker<G> worker(
            Function<String, ContinuableListing<G>> batchLoader,
            Consumer<List<G>> batchProcessor){
        return new ContinuableBatchWorker<>(batchLoader, batchProcessor);
    }

    /***************************************************************************
     *                                                                         *
     * Fields                                                                  *
     *                                                                         *
     **************************************************************************/

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final Function<String, ContinuableListing<T>> batchLoader;
    private final Consumer<List<T>> batchProcessor;

    /***************************************************************************
     *                                                                         *
     * Public API                                                              *
     *                                                                         *
     **************************************************************************/

    public ContinuableBatchWorker(
        Function<String, ContinuableListing<T>> batchLoader,
        Consumer<List<T>> batchProcessor
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
    public Metrics processAll() throws BatchWorkerException {
        String nextToken = null;
        var metrics = new Metrics();
        do {

            long start = System.nanoTime();

            var chunk = loadNext(nextToken);
            var items = chunk.getContent();

            processAll(items);

            metrics.reportProcessedBatch(items.size(), System.nanoTime()-start);

            nextToken = chunk.getNextContinuationToken();

        } while (nextToken != null);

        return metrics;
    }

    /***************************************************************************
     *                                                                         *
     * Private API                                                             *
     *                                                                         *
     **************************************************************************/

    private ContinuableListing<T> loadNext(String nextToken){
        try {
            var chunk = batchLoader.apply(nextToken);
            logger.debug("Received chunk, size: {}, hasMore: {}, nextContinuationToken:",
                    chunk.getMaxChunkSize(), chunk.hasMore(), chunk.getNextContinuationToken());
            return chunk;
        }catch (Exception e){
            throw new BatchWorkerException("Failed to load next batch with nextToken: " + nextToken, e);
        }
    }

    private void processAll(List<T> items){
        try {
            batchProcessor.accept(items);
        }catch (Exception e){
            throw new BatchWorkerException("Failed to process item batch!", e);
        }
    }

    /***************************************************************************
     *                                                                         *
     * Inner class                                                             *
     *                                                                         *
     **************************************************************************/

    public static class Metrics {
        private int processedItems;
        private int processedBatches;
        private long batchMaxTimeMs = 0;
        private long batchMinTimeMs = Long.MAX_VALUE;


        public void reportProcessedBatch(int items, long nanoTime){
            processedItems += items;
            processedBatches++;

            var msTime = nanoTime / (1000*1000);
            batchMaxTimeMs = Math.max(batchMaxTimeMs, msTime);
            batchMinTimeMs = Math.min(batchMinTimeMs, msTime);
        }

        public int getProcessedItems() {
            return processedItems;
        }

        public int getProcessedBatches() {
            return processedBatches;
        }

        public long getBatchMaxTimeMs() {
            return batchMaxTimeMs;
        }

        public long getBatchMinTimeMs() {
            return batchMinTimeMs;
        }
    }
}
