package com.elderbyte.spring.cloud.bootstrap.feign;

import com.elderbyte.commons.data.contiunation.ContinuationToken;
import feign.RequestTemplate;
import feign.codec.EncodeException;
import feign.codec.Encoder;

import java.lang.reflect.Type;

/**
 * This encoder adds support for pageable, which will be applied to the query parameters.
 * // TODO can be removed once Feign supports pageable native!
 */
public class ContinuationTokenQueryEncoder implements Encoder {
    private final Encoder delegate;

    public ContinuationTokenQueryEncoder(Encoder delegate){
        this.delegate = delegate;
    }

    @Override
    public void encode(Object object, Type bodyType, RequestTemplate template) throws EncodeException {
        if(object instanceof ContinuationToken){
            ContinuationToken pageable = (ContinuationToken)object;
            template.query("continuationToken", pageable.getToken() + "");
        }else{
            delegate.encode(object, bodyType, template);
        }
    }
}


