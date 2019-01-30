package com.elderbyte.spring.boot.bootstrap.data.pagination;


import org.springframework.data.domain.Pageable;

public class PageableDto {
    public int pageIndex;
    public int pageSize;

    public PageableDto(){}

    public PageableDto(int pageIndex, int pageSize){
        this.pageIndex = pageIndex;
        this.pageSize = pageSize;
    }

    public PageableDto(Pageable pageable){
        pageIndex = pageable.getPageNumber();
        pageSize = pageable.getPageSize();
    }
}
