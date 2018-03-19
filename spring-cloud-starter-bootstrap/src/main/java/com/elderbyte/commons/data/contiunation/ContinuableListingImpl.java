package com.elderbyte.commons.data.contiunation;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class ContinuableListingImpl<T> implements ContinuableListing<T> {

    /***************************************************************************
     *                                                                         *
     * Fields                                                                  *
     *                                                                         *
     **************************************************************************/

    private List<T> content;
    private String nextContinuationToken;
    private String continuationToken;
    private int maxChunkSize;
    private Long total;

    /***************************************************************************
     *                                                                         *
     * Constructors                                                            *
     *                                                                         *
     **************************************************************************/


    public ContinuableListingImpl(
            @JsonProperty("content") List<T> content,
            @JsonProperty("continuationToken") String currentToken,
            @JsonProperty("maxChunkSize") int maxChunkSize,
            @JsonProperty("total") Long total,
            @JsonProperty("nextContinuationToken") String nextContinuationToken
            ){

        if(content == null) throw new IllegalArgumentException("content must not be null");

        this.content = content;
        this.continuationToken = currentToken;
        this.nextContinuationToken = nextContinuationToken;
        this.maxChunkSize = maxChunkSize;
        this.total = total;
    }

    /***************************************************************************
     *                                                                         *
     * Properties                                                              *
     *                                                                         *
     **************************************************************************/


    @Override
    @JsonProperty
    public List<T> getContent() {
        return content;
    }

    @Override
    @JsonProperty
    public String getContinuationToken() {
        return continuationToken;
    }

    @Override
    @JsonProperty
    public String getNextContinuationToken() {
        return nextContinuationToken;
    }

    @Override
    @JsonProperty
    public int getMaxChunkSize() {
        return maxChunkSize;
    }

    @Override
    @JsonProperty
    public boolean hasMore() {
        return nextContinuationToken != null;
    }

    @Override
    @JsonProperty
    public Long getTotal() {
        return total;
    }
}
