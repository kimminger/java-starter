package com.elderbyte.commons.data.contiunation;

import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.*;

public class ContinuableListingTest {


    @Test
    public void empty() {
        var empty = ContinuableListing.empty();

        assertEquals(0, empty.getContent().size());
        assertEquals((Long)(long)0, empty.getTotal());
        assertEquals(0, empty.getMaxChunkSize());
        assertNull(empty.getNextContinuationToken());
        assertNull(empty.getContinuationToken());
    }

    @Test
    public void finiteChunk() {


    }

    @Test
    public void finiteChunk1() {
    }

    @Test
    public void finiteChunk2() {
    }

    @Test
    public void finiteChunk3() {
    }

    @Test
    public void finiteChunk4() {
    }

    @Test
    public void continuable() {

        var chunk = Arrays.asList("a", "b", "c");

        var token = "1234";
        var tokenNext = "6789";

        var mockListing = ContinuableListing.continuable(
                chunk,
                3,
                340000000L,
                ContinuationToken.from(token),
                ContinuationToken.from(tokenNext)
        );

        assertEquals(3, mockListing.getContent().size());
        assertEquals((Long)(long)340000000L, mockListing.getTotal());
        assertEquals(3, mockListing.getMaxChunkSize());
        assertEquals(tokenNext, mockListing.getNextContinuationToken());
        assertEquals(token, mockListing.getContinuationToken());

    }

    @Test
    public void continuable1() {
    }

    @Test
    public void map() {
    }

    @Test
    public void filter() {
    }

    @Test
    public void withCurrentToken() {
    }

    @Test
    public void withNextToken() {
    }

    @Test
    public void withContent() {
    }

    @Test
    public void withTotal() {
    }
}
