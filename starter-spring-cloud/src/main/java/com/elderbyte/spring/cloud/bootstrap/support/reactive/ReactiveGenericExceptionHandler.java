package com.elderbyte.spring.cloud.bootstrap.support.reactive;

import com.elderbyte.commons.exceptions.ExceptionUtil;
import com.elderbyte.commons.exceptions.NotFoundException;
import feign.FeignException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.SocketTimeoutException;
import java.nio.charset.StandardCharsets;

@Order(Ordered.LOWEST_PRECEDENCE) // Ensure our generic Exception handler gets lowest PRECEDENCE
@ControllerAdvice
public class ReactiveGenericExceptionHandler {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @ExceptionHandler(value = { NotFoundException.class } )
    public Mono<Void> handleNotFound(
            ServerWebExchange webExchange,
            Exception exception) {

        var request = webExchange.getRequest();
        var response = webExchange.getResponse();

        String agrMessages = ExceptionUtil.aggregateMessages(exception);
        var message = "NotFoundException: " + agrMessages + " resource: " +  request.getURI();

        logger.warn(message);

        return sendResponse(response, HttpStatus.NOT_FOUND, message);
    }

    @ExceptionHandler(value = { SocketTimeoutException.class } )
    public Mono<Void> timeoutException(
            ServerWebExchange webExchange,
            Exception exception) {

        var request = webExchange.getRequest();
        var response = webExchange.getResponse();

        String agrMessages = ExceptionUtil.aggregateMessages(exception);

        var message = exception.getClass().getSimpleName() + ": " + agrMessages + " resource: " +  request.getURI();

        logger.error(message, exception);

        return sendResponse(response, HttpStatus.GATEWAY_TIMEOUT, message);
    }

    @ExceptionHandler(value = { FeignException.class } )
    public Mono<Void> gatewayException(
            ServerWebExchange webExchange,
            Exception exception) {

        var request = webExchange.getRequest();
        var response = webExchange.getResponse();

        var message = exception.getClass().getSimpleName() + ": " + ExceptionUtil.aggregateMessages(exception) + " resource: " +  request.getURI();

        logger.error(message, exception);

        return sendResponse(response, HttpStatus.BAD_GATEWAY, message);
    }

    @ExceptionHandler(value = {Exception.class, RuntimeException.class} )
    public Mono<Void> handleUnhandled(
            ServerWebExchange webExchange,
            Exception exception) {

        var request = webExchange.getRequest();
        var response = webExchange.getResponse();

        String agrMessages = ExceptionUtil.aggregateMessages(exception);

        var message = exception.getClass().getSimpleName() + ": " + agrMessages + " resource: " +  request.getURI();

        logger.error(message, exception);

        return sendResponse(response, HttpStatus.INTERNAL_SERVER_ERROR, message);
    }


    private Mono<Void> sendResponse(ServerHttpResponse response, HttpStatus httpStatus, String errorMessage){

        response.setStatusCode(httpStatus);


        byte[] bytes = errorMessage.getBytes(StandardCharsets.UTF_8);
        var buffer = response.bufferFactory().wrap(bytes);
        return response.writeWith(Flux.just(buffer));
    }
}



