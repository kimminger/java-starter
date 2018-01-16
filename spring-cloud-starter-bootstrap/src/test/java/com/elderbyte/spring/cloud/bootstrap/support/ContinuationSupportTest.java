package com.elderbyte.spring.cloud.bootstrap.support;

import com.elderbyte.commons.data.contiunation.ContinuableListing;
import com.elderbyte.commons.data.contiunation.ContinuationToken;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;

import static org.junit.Assert.*;

public class ContinuationSupportTest {

    @Test
    public void toPageable() {
        ContinuationToken continuationToken = ContinuationToken.from("10-35");
        Pageable pageable = ContinuationSupport.toPageable(continuationToken);

        assertEquals(10, pageable.getPageNumber());
        assertEquals(35, pageable.getPageSize());
    }

    @Test
    public void fromPageable() {
        ContinuationToken continuationToken = ContinuationSupport.fromPageable(PageRequest.of(10, 35));
        assertEquals("10-35", continuationToken.getToken());
    }

    @Test
    public void fromPage(){

        // Given: A Page

        Page<String> myPage = new PageImpl<>(
                Arrays.asList("a", "b", "c"),
                PageRequest.of(1, 3), // page == 1 => 2nd page (zero index)
                9
        );


        ContinuableListing<String> listing = ContinuationSupport.fromPage(myPage);


        Assert.assertEquals(3, listing.getChunkSize());
        Assert.assertEquals(3, listing.getContent().size());
        Assert.assertEquals(true, listing.hasMore());
        Assert.assertEquals("1-3", listing.getContinuationToken());
        Assert.assertEquals("2-3", listing.getNextContinuationToken());
    }
}