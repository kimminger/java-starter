package com.elderbyte.spring.cloud.bootstrap.support;


import org.springframework.data.domain.Pageable;

public class PageableDto {
    public int pageIndex;
    public int pageSize;

    public PageableDto(){}

    public PageableDto(Pageable pageable){
        pageIndex = pageable.getPageNumber();
        pageSize = pageable.getPageSize();
    }
}
