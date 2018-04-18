package com.elderbyte.spring.cloud.bootstrap.feign;

import feign.Client;
import feign.Feign;
import feign.codec.Decoder;
import feign.codec.Encoder;
import feign.hystrix.HystrixFeign;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FeignBuilderService {

    /***************************************************************************
     *                                                                         *
     * Fields                                                                  *
     *                                                                         *
     **************************************************************************/

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private Client client;

    @Autowired
    private Decoder feignDecoder;

    @Autowired
    private Encoder feignEncoder;

    private final static Pattern discoveryUrl = Pattern.compile("discovery://(.+?)(/.*)");

    /***************************************************************************
     *                                                                         *
     * Public API                                                              *
     *                                                                         *
     **************************************************************************/

    public <T> T feignClient(Class<T> clazz, String rootUrl){

        log.info("Creating feign client for url: " + rootUrl);

        try{
            Matcher matcher = discoveryUrl.matcher(rootUrl);
            if(matcher.matches()){
                return feignClientDiscovery(clazz, matcher.group(1), matcher.group(2));
            }else{
                return feignClientDefault(clazz, rootUrl);
            }
        }catch (Exception e){
            throw new RuntimeException("Failed to build feign client for url: " + rootUrl, e);
        }
    }

    /***************************************************************************
     *                                                                         *
     * Private Methods                                                         *
     *                                                                         *
     **************************************************************************/

    private <T> T feignClientDefault(Class<T> clazz, String externalUrl){

        log.debug("Creating Default Feign with URL: " + externalUrl);

        return Feign.builder()
                .client(client)
                .encoder(feignEncoder)
                .decoder(feignDecoder)
                .target(clazz, externalUrl);
    }


    private  <T> T feignClientDiscovery(Class<T> clazz, String serviceId, String path){

        String hystrixUrl = "http://" + serviceId + path;

        log.debug("Creating Hystrix Feign with URL: " + hystrixUrl);

        return HystrixFeign.builder()
                .client(client)
                .encoder(feignEncoder)
                .decoder(feignDecoder)
                .target(clazz, hystrixUrl);
    }

}
