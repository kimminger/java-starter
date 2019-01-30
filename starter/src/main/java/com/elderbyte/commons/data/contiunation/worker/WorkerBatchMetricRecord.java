package com.elderbyte.commons.data.contiunation.worker;

import com.elderbyte.commons.data.contiunation.ContinuableListing;

import java.util.Optional;

/**
 * Represents metric record of a single batch.
 *
 * This class is immutable
 */
public class WorkerBatchMetricRecord {

    /***************************************************************************
     *                                                                         *
     * Static builders                                                         *
     *                                                                         *
     **************************************************************************/

    public static WorkerBatchMetricRecord fromListing(ContinuableListing<?> listing, long loadingTime, long processingTime){
        return new WorkerBatchMetricRecord(
                listing.getTotal(),
                listing.getContent().size(),
                loadingTime,
                processingTime
            );
    }

    /***************************************************************************
     *                                                                         *
     * Fields                                                                  *
     *                                                                         *
     **************************************************************************/

    /**
     * The current estimated total of all items.
     */
    private final Long totalItems;

    /**
     * The number of items in this batch
     */
    private final int batchSize;

    /**
     * The duration of this batch loading [nano]
     */
    private final long batchLoadingTime;

    /**
     * The duration of this batch processing [nano]
     */
    private final long batchProcessingTime;

    /***************************************************************************
     *                                                                         *
     * Constructor                                                             *
     *                                                                         *
     **************************************************************************/

    public WorkerBatchMetricRecord(Long totalItems, int batchSize, long batchLoadingTime, long batchProcessingTime) {
        this.totalItems = totalItems;
        this.batchSize = batchSize;
        this.batchLoadingTime = batchLoadingTime;
        this.batchProcessingTime = batchProcessingTime;
    }

    /***************************************************************************
     *                                                                         *
     * Properties                                                              *
     *                                                                         *
     **************************************************************************/

    public Optional<Long> getTotalItems() {
        return Optional.ofNullable(totalItems);
    }

    public int getBatchSize() {
        return batchSize;
    }

    public long getBatchLoadingTime() {
        return batchLoadingTime;
    }

    public long getBatchProcessingTime() {
        return batchProcessingTime;
    }
}
