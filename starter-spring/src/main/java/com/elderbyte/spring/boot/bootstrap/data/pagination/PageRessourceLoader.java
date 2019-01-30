package com.elderbyte.spring.boot.bootstrap.data.pagination;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class PageRessourceLoader {

    private static final Logger logger = LoggerFactory.getLogger(PageRessourceLoader.class);

    public static <T> List<T> loadPages(int pageSize, Function<Pageable, Page<T>>
            pageLoader){

        Page<T> page = pageLoader.apply(PageRequest.of(0, pageSize));
        List<T> totalResult = new ArrayList<>(page.getContent());

        while(page.hasNext()) {

            page = pageLoader.apply(page.nextPageable());
            totalResult.addAll(page.getContent());
        }

        return totalResult;

    }


}
