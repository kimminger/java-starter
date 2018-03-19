package com.elderbyte.spring.cloud.bootstrap.support;

import com.elderbyte.commons.data.contiunation.ContinuableListing;
import com.elderbyte.commons.data.contiunation.ContinuationToken;
import org.springframework.data.domain.Page;


public class ContinuablePageSupport {
    /**
     * Converts the given page into a ContinuableListing
     * @param page The page.
     */
    public static <T> ContinuableListing<T> fromPage(Page<T> page){
        ContinuationToken currentToken = PageableContinuationToken.fromPageable(page.getPageable());
        ContinuationToken nextToken = null;
        if(page.hasNext()){
            nextToken = PageableContinuationToken.fromPageable(page.getPageable().next());
        }

        if(nextToken != null){
            return ContinuableListing.continuable(page.getContent(), page.getSize(), currentToken, nextToken);
        }else{
            return ContinuableListing.finiteChunk(page.getContent(), page.getSize(), currentToken);
        }
    }
}
