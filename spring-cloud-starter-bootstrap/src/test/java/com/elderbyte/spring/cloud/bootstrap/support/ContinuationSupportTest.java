package com.elderbyte.spring.cloud.bootstrap.support;

import com.elderbyte.commons.data.contiunation.ContinuationToken;
import org.junit.Test;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

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
}