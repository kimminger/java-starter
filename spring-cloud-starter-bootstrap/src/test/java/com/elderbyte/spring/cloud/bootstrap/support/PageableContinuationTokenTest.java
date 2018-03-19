package com.elderbyte.spring.cloud.bootstrap.support;

import com.elderbyte.commons.data.contiunation.ContinuableListing;
import com.elderbyte.commons.data.contiunation.ContinuationToken;
import com.elderbyte.commons.utils.Utf8Base64;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.Base64;

import static org.junit.Assert.*;

public class PageableContinuationTokenTest {


    @Test
    public void toPageable() {
        ContinuationToken token = ContinuationToken.from(Utf8Base64.encodeUtf8("{\"pageIndex\":10,\"pageSize\":35}"));
        PageableContinuationToken continuationToken = PageableContinuationToken.from(token);
        Pageable pageable = continuationToken.asPageable().orElse(null);

        assertEquals(10, pageable.getPageNumber());
        assertEquals(35, pageable.getPageSize());
    }

    @Test
    public void fromPageable() {
        ContinuationToken token = PageableContinuationToken.buildToken(PageRequest.of(10, 35));
        PageableContinuationToken continuationToken = PageableContinuationToken.from(token);
        Pageable pageable = continuationToken.asPageable().orElseThrow(() -> new IllegalStateException(""));
        assertEquals(10, pageable.getPageNumber());
        assertEquals(35, pageable.getPageSize());
    }

    @Test
    public void fromPage(){

        // Given: A Page

        Page<String> myPage = new PageImpl<>(
                Arrays.asList("a", "b", "c"),
                PageRequest.of(1, 3), // page == 1 => 2nd page (zero index)
                9
        );


        ContinuableListing<String> listing = ContinuablePageSupport.fromPage(myPage);


        Assert.assertEquals(3, listing.getMaxChunkSize());
        Assert.assertEquals(3, listing.getContent().size());
        Assert.assertEquals(true, listing.hasMore());
        Assert.assertEquals("{\"pageIndex\":1,\"pageSize\":3}", Utf8Base64.decodeUtf8(listing.getContinuationToken()));
        Assert.assertEquals("{\"pageIndex\":2,\"pageSize\":3}", Utf8Base64.decodeUtf8(listing.getNextContinuationToken()));
    }


}