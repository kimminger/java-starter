package com.elderbyte.spring.cloud.bootstrap.support;

import com.elderbyte.commons.data.contiunation.ContinuableListing;
import com.elderbyte.commons.data.contiunation.ContinuationToken;
import com.elderbyte.commons.exceptions.ArgumentNullException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public class ContinuationSupport
{

    /**
     * Decodes a continuation-token into an unsorted pageable
     */
    public static Pageable toPageable(ContinuationToken token){
        return toPageable(token, Sort.unsorted());
    }

    /**
     * Decodes a continuation-token into a pageable
     * @param token The token with the page information
     * @param sort The sort to use
     */
    public static Pageable toPageable(ContinuationToken token, Sort sort){

        if(token == null) throw new ArgumentNullException("token");
        if(sort == null) throw new ArgumentNullException("sort");

        String[] parts = token.getToken().split("-");
        if(parts.length == 2){
            try {
                int page = Integer.parseInt(parts[0]);
                int size = Integer.parseInt(parts[1]);
                return PageRequest.of(page, size, sort);
            }catch (NumberFormatException e){
                throw new IllegalArgumentException("The given token was not in a valid page-size format: " + token, e);
            }
        }else{
            throw new IllegalArgumentException("The given token was not in a valid page-size format: " + token);
        }
    }

    /**
     * Encodes the given pageable into a continuation-token.
     */
    public static ContinuationToken fromPageable(Pageable pageable){
        return ContinuationToken.from(pageable.getPageNumber() + "-" + pageable.getPageSize());
    }

    /**
     * Converts the given page into a ContinuableListing
     * @param page The page.
     */
    public static <T> ContinuableListing<T> fromPage(Page<T> page){
        ContinuationToken currentToken = fromPageable(page.getPageable());
        ContinuationToken nextToken = null;
        if(page.hasNext()){
            nextToken = fromPageable(page.getPageable().next());
        }

        if(nextToken != null){
            return ContinuableListing.continuable(page.getContent(), page.getSize(), currentToken, nextToken);
        }else{
            return ContinuableListing.finiteChunk(page.getContent(), page.getSize(), currentToken);
        }
    }



}
