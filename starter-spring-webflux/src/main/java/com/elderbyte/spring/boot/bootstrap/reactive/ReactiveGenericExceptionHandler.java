package com.elderbyte.spring.boot.bootstrap.reactive;

import com.elderbyte.commons.errors.WebErrorDetail;
import com.elderbyte.commons.exceptions.NotFoundException;
import com.elderbyte.web.client.HttpResponseError;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.nio.charset.StandardCharsets;

@Order(-2) // Ensure our generic Exception handler is not overridden by spring default ones.
@ControllerAdvice
public class ReactiveGenericExceptionHandler implements ErrorWebExceptionHandler {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final ObjectMapper mapper;

    public ReactiveGenericExceptionHandler(ObjectMapper mapper){
        logger.info("Configuring webflux ReactiveGenericExceptionHandler!");
        this.mapper = mapper;
    }

    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {

        HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;

        if(ex instanceof NotFoundException){
            httpStatus = HttpStatus.NOT_FOUND;
        }else if(ex instanceof SocketTimeoutException){
            httpStatus = HttpStatus.GATEWAY_TIMEOUT;
        }else if(ex instanceof HttpResponseError){
            httpStatus = HttpStatus.BAD_GATEWAY;
        }

        return sendErrorDetail(exchange, ex, httpStatus);
    }

    private Mono<Void> sendErrorDetail(ServerWebExchange exchange, Throwable exception, HttpStatus status){
        var request = exchange.getRequest().getMethod() + " " + exchange.getRequest().getPath();

        var error = WebErrorDetail.build(
                status.value(),
                status.getReasonPhrase(),
                exception,
                request
        );

        if(logger.isDebugEnabled()){
            logger.error(error.toString(), exception);
        }else{
            logger.error(error.toString());
        }

        return writeAsJson(exchange, error, status);
    }

    private Mono<Void> writeAsJson(ServerWebExchange exchange,  Object entity, HttpStatus status) {
        try {
            var json = mapper.writeValueAsString(entity);
            exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);
            return writeBody(exchange, json, status);
        }catch (IOException e){
            return Mono.error(e);
        }
    }

    private static Mono<Void> writeBody(ServerWebExchange exchange, String text, HttpStatus status)  {
        byte[] bytes = text.getBytes(StandardCharsets.UTF_8);
        DataBuffer buffer = exchange.getResponse().bufferFactory().wrap(bytes);
        exchange.getResponse().setStatusCode(status);
        exchange.getResponse().getHeaders().setContentLength(bytes.length);
        return exchange.getResponse().writeWith(Flux.just(buffer));
    }

}



