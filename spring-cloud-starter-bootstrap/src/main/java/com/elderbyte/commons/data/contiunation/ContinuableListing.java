package com.elderbyte.commons.data.contiunation;

import java.util.List;

public interface ContinuableListing<T> {

    static <T> ContinuableListing<T> finiteChunk(List<T> content){
        return continuable(content, content.size(), ContinuationToken.Empty, ContinuationToken.Empty);
    }

    static <T> ContinuableListing<T> finiteChunk(List<T> content, int maxChunkSize){
        return continuable(content, maxChunkSize, ContinuationToken.Empty, ContinuationToken.Empty);
    }

    static <T> ContinuableListing<T> finiteChunk(List<T> content, int maxChunkSize, ContinuationToken current){
        return continuable(content, maxChunkSize, current, ContinuationToken.Empty);
    }

    static <T> ContinuableListing<T> finiteChunk(List<T> content, int maxChunkSize, Long total, ContinuationToken current){
        return continuable(content, maxChunkSize, total, current, ContinuationToken.Empty);
    }

    static <T> ContinuableListing<T> continuable(List<T> content, int maxChunkSize, ContinuationToken current, ContinuationToken nextChunkToken){
        return continuable(content, maxChunkSize, null, current, nextChunkToken);
    }

    static <T> ContinuableListing<T> continuable(List<T> content, int maxChunkSize, Long total, ContinuationToken current, ContinuationToken nextChunkToken){
        return new ContinuableListingImpl<>(
                content,
                current.getTokenIfNotEmpty().orElse(null),
                maxChunkSize,
                total,
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
    int getMaxChunkSize();

    /**
     * The total number of elements, if known.
     * Otherwise, might be NULL!
     */
    Long getTotal();

    /**
     * Is there more data to load with the NextContiunationToken?
     */
    boolean hasMore();
}