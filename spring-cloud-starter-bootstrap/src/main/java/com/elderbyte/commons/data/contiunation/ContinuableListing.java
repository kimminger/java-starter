package com.elderbyte.commons.data.contiunation;

import java.util.List;

public interface ContinuableListing<T> {

    static <T> ContinuableListing<T> finiteChunk(List<T> content, int maxChunkSize, ContinuationToken current){
        return continuable(content, maxChunkSize, current, ContinuationToken.Empty);
    }

    static <T> ContinuableListing<T> continuable(List<T> content, int maxChunkSize, ContinuationToken current, ContinuationToken nextChunkToken){
        return new ContinuableListingImpl<>(
                content,
                current.getTokenIfNotEmpty().orElse(null),
                maxChunkSize,
                nextChunkToken.getTokenIfNotEmpty().orElse(null)
        );
    }

    /**
     * The content in this listing chunk
     */
    List<T> getContent();

    /**
     * The current continuation token of this listing.
     * To fetch the next page, use the NextContinuationToken property not this!
     */
    String getContinuationToken();

    /**
     * The continuation token to fetch the next part
     */
    String getNextContinuationToken();

    /**
     * Size of this chunk (max page size)
     */
    int getChunkSize();

    /**
     * Is there more data to load with the NextContiunationToken?
     */
    boolean hasMore();
}