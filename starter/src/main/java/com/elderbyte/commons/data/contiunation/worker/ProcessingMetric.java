package com.elderbyte.commons.data.contiunation.worker;

import com.elderbyte.commons.data.contiunation.ContinuableListing;
import com.elderbyte.commons.exceptions.ArgumentNullException;

import java.util.Optional;

/**
 * Represents metric record of a single batch.
 *
 * This class is immutable
 */
public class ProcessingMetric extends BaseMetric {

    /***************************************************************************
     *                                                                         *
     * Static builders                                                         *
     *                                                                         *
     **************************************************************************/

    public static ProcessingMetric fromListing(ContinuableListing<?> listing, long processingTime){

        if(listing == null) throw new ArgumentNullException("listing");

        return new ProcessingMetric(
                listing.getTotal(),
                listing.getContent().size(),
                processingTime
            );
    }

    public static ProcessingMetric error(Throwable throwable, ContinuableListing<?> listing, long processingTime){
        return new ProcessingMetric(
                throwable,
                listing != null ? listing.getTotal() : null,
                listing != null ? listing.getContent().size() : 0,
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
     * The duration of this batch processing [nano]
     */
    private final long batchProcessingTime;

    /***************************************************************************
     *                                                                         *
     * Constructor                                                             *
     *                                                                         *
     **************************************************************************/

    private ProcessingMetric(Long totalItems, int batchSize, long batchProcessingTime) {
        this(null, totalItems, batchSize, batchProcessingTime);
    }
    private ProcessingMetric(Throwable throwable, Long totalItems, int batchSize, long batchProcessingTime) {
        super(throwable);
        this.totalItems = totalItems;
        this.batchSize = batchSize;
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

    public long getBatchProcessingTime() {
        return batchProcessingTime;
    }
}
