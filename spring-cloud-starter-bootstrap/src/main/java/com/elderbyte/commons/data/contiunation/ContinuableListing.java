package com.elderbyte.commons.data.contiunation;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

import static java.util.stream.Collectors.toList;

@JsonDeserialize(as = ContinuableListingImpl.class)
public interface ContinuableListing<T> {

    /***************************************************************************
     *                                                                         *
     * Static builders                                                         *
     *                                                                         *
     **************************************************************************/

    static <T> ContinuableListing<T> empty(){
        return finiteChunk(new ArrayList<>());
    }

    static <T> ContinuableListing<T> finiteChunk(T... content){
        return finiteChunk(Arrays.asList(content));
    }

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

    /***************************************************************************
     *                                                                         *
     * Public Interface                                                        *
     *                                                                         *
     **************************************************************************/

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


    /**
     * Returns a new {@link ContinuableListing} with the content of the current one mapped by the given {@link Function}.
     *
     * @param converter must not be {@literal null}.
     * @return a new {@link ContinuableListing} with the content of the current one mapped by the given {@link Function}.
     */
    default <U> ContinuableListing<U> map(Function<? super T, ? extends U> converter){
        return new ContinuableListingImpl<>(
                this.getContent().stream().map(converter).collect(toList()),
                this.getContinuationToken(),
                this.getMaxChunkSize(),
                this.getTotal(),
                this.getNextContinuationToken()
        );
    }

    /**
     * Returns a new {@link ContinuableListing} with the content of the current one filtered by the given {@link Predicate}.
     * @param predicate The filter function
     */
    default ContinuableListing<T> filter(Predicate<T> predicate){
        return new ContinuableListingImpl<>(
                this.getContent().stream().filter(predicate).collect(toList()),
                this.getContinuationToken(),
                this.getMaxChunkSize(),
                this.getTotal(),
                this.getNextContinuationToken()
        );
    }
}