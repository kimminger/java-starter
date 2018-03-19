package com.elderbyte.spring.cloud.bootstrap.support;

import com.elderbyte.commons.data.contiunation.ContinuableListing;
import com.elderbyte.commons.data.contiunation.ContinuationToken;
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
        PageableContinuationToken continuationToken = PageableContinuationToken.from(encodeUtf8("{\"pageIndex\":10,\"pageSize\":35}"));
        Pageable pageable = continuationToken.asPageable().orElse(null);

        assertEquals(10, pageable.getPageNumber());
        assertEquals(35, pageable.getPageSize());
    }

    @Test
    public void fromPageable() {
        PageableContinuationToken continuationToken = PageableContinuationToken.fromPageable(PageRequest.of(10, 35));
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


        Assert.assertEquals(3, listing.getChunkSize());
        Assert.assertEquals(3, listing.getContent().size());
        Assert.assertEquals(true, listing.hasMore());
        Assert.assertEquals("{\"pageIndex\":1,\"pageSize\":3}", decodeUtf8(listing.getContinuationToken()));
        Assert.assertEquals("{\"pageIndex\":2,\"pageSize\":3}", decodeUtf8(listing.getNextContinuationToken()));
    }

    private String decodeUtf8(String base64){
        try {
            return new String(Base64.getDecoder().decode(base64), "UTF8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    private String encodeUtf8(String raw){
        try {
            return new String(Base64.getEncoder().encode(raw.getBytes("UTF8")));
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }
}