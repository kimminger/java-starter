package com.elderbyte.web;

import com.elderbyte.commons.data.contiunation.ContinuationToken;
import org.junit.Test;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.util.UriBuilder;
import org.springframework.web.util.UriComponentsBuilder;

import static org.junit.Assert.*;

public class UriBuilderSupportTest {

    @Test
    public void apply_pageable() {
        var pageRequest = PageRequest.of(2, 33, Sort.Direction.DESC, "my.test");
        var builder = UriComponentsBuilder.fromUriString("http://elderbyte.com/test");
        var applied = UriBuilderSupport.apply(builder, pageRequest);
        var uri  = applied.build().toString();
        assertEquals("http://elderbyte.com/test?page=2&size=33&sort=my.test,desc", uri);
    }

    @Test
    public void apply_sort() {
        var sort = Sort.by(Sort.Direction.DESC, "my.test");
        var builder = UriComponentsBuilder.fromUriString("http://elderbyte.com/test");
        var applied = UriBuilderSupport.apply(builder, sort);
        var uri  = applied.build().toString();
        assertEquals("http://elderbyte.com/test?sort=my.test,desc", uri);
    }

    @Test
    public void apply_sort_multiple() {
        var sort = Sort.by(Sort.Order.asc("my.test"), Sort.Order.desc("low"));
        var builder = UriComponentsBuilder.fromUriString("http://elderbyte.com/test");
        var applied = UriBuilderSupport.apply(builder, sort);
        var uri  = applied.build().toString();
        assertEquals("http://elderbyte.com/test?sort=my.test,asc&sort=low,desc", uri);
    }

    @Test
    public void apply_token() {
        var token = ContinuationToken.from("my-token");
        var builder = UriComponentsBuilder.fromUriString("http://elderbyte.com/test");
        var applied = UriBuilderSupport.apply(builder, token);
        var uri  = applied.build().toString();
        assertEquals("http://elderbyte.com/test?continuationToken=my-token", uri);
    }

}
