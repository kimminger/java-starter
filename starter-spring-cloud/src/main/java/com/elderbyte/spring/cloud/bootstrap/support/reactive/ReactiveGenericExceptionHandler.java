package com.elderbyte.spring.cloud.bootstrap.support.reactive;

import com.elderbyte.commons.exceptions.ExceptionUtil;
import com.elderbyte.commons.exceptions.NotFoundException;
import feign.FeignException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.SocketTimeoutException;
import java.nio.charset.StandardCharsets;

@Order(Ordered.LOWEST_PRECEDENCE) // Ensure our generic Exception handler gets lowest PRECEDENCE
@ControllerAdvice
public class ReactiveGenericExceptionHandler implements ErrorWebExceptionHandler {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public ReactiveGenericExceptionHandler(){
        logger.info("Configuring webflux ReactiveGenericExceptionHandler!");

    }

    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {
        var request = exchange.getRequest();
        var response = exchange.getResponse();

        String agrMessages = ExceptionUtil.aggregateMessages(ex);
        String message = ex.getClass().getSimpleName() + ": " + agrMessages + " resource: " +  request.getURI();
        HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;

        if(ex instanceof NotFoundException){
            httpStatus = HttpStatus.NOT_FOUND;
        }else if(ex instanceof SocketTimeoutException){
            httpStatus = HttpStatus.GATEWAY_TIMEOUT;
        }else if(ex instanceof FeignException){
            httpStatus = HttpStatus.BAD_GATEWAY;
        }

        return sendResponse(response, httpStatus, message);
    }

    private Mono<Void> sendResponse(ServerHttpResponse response, HttpStatus httpStatus, String errorMessage){

        response.setStatusCode(httpStatus);

        byte[] bytes = errorMessage.getBytes(StandardCharsets.UTF_8);
        var buffer = response.bufferFactory().wrap(bytes);
        return response.writeWith(Flux.just(buffer));
    }


}



