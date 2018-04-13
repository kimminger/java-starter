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
        return worker(batchLoader, batchProcessor);
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
        return processAll(metrics -> {});
    }

    /**
     * Process all items from the continuable source.
     * @param progressCallback Callback for metrics report while the worker is processing
     * @throws BatchWorkerException Thrown when there was an issue processing a batch.
     */
    public Metrics processAll(Consumer<Metrics> progressCallback) throws BatchWorkerException {
        String nextToken = null;
        var reporter = new MetricsReporter();

        do {

            long start = System.nanoTime();
            var chunk = loadNext(nextToken);
            var items = chunk.getContent();

            processAll(items);

            reporter.reportProcessedBatch(items.size(), System.nanoTime()-start);

            nextToken = chunk.getNextContinuationToken();

            progressCallback.accept(reporter.getSnapshot());

        } while (nextToken != null);

        return reporter.getSnapshot();
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

    static class MetricsReporter {

        private int processedItems;
        private int processedBatches;
        private long batchMaxTimeMs = 0;
        private long batchMinTimeMs = Long.MAX_VALUE;
        private long totalTimeNano = 0;

        public void reportProcessedBatch(int items, long nanoTime){
            totalTimeNano += nanoTime;
            processedItems += items;
            processedBatches++;

            var msTime = nanoToMillis(nanoTime);
            batchMaxTimeMs = Math.max(batchMaxTimeMs, msTime);
            batchMinTimeMs = Math.min(batchMinTimeMs, msTime);
        }

        public Metrics getSnapshot(){
            return new Metrics(
                    processedItems,
                    processedBatches,
                    batchMaxTimeMs,
                    batchMinTimeMs,
                    nanoToMillis(totalTimeNano)
            );
        }

        private long nanoToMillis(long nano){
            return nano / (1000*1000);
        }
    }

    public static class Metrics {

        private final int processedItems;
        private final int processedBatches;
        private final long batchMaxTimeMs;
        private final long batchMinTimeMs;
        private final long totalTimeMs;

        public Metrics(int processedItems, int processedBatches, long batchMaxTimeMs, long batchMinTimeMs, long totalTimeMs) {
            this.processedItems = processedItems;
            this.processedBatches = processedBatches;
            this.batchMaxTimeMs = batchMaxTimeMs;
            this.batchMinTimeMs = batchMinTimeMs;
            this.totalTimeMs = totalTimeMs;
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

        public long getTotalTimeMs() {
            return totalTimeMs;
        }
    }
}
