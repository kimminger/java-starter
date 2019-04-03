package com.elderbyte.spring.bootstrap.servlet;

import com.elderbyte.commons.exceptions.NotFoundException;
import com.elderbyte.spring.boot.bootstrap.errors.ExceptionDetailDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.SocketTimeoutException;


/**
 * A controller advice which deals with unhandled exceptions and transforms them into
 * HTTP responses.
 */
@Order(Ordered.LOWEST_PRECEDENCE) // Ensure our generic Exception handler gets lowest PRECEDENCE
@ControllerAdvice
public class ServletGenericExceptionHandler {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * Map NotFoundException to NOT_FOUND HTTP CODE
     */
    @ResponseBody
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NotFoundException.class)
    public ExceptionDetailDto handleNotFound(
            HttpServletRequest req,
            Exception exception) {

        var error = build(HttpStatus.NOT_FOUND, exception, req);
        logger.warn(error.message);
        return error;
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.GATEWAY_TIMEOUT)
    @ExceptionHandler(SocketTimeoutException.class)
    public ExceptionDetailDto timeoutException(
            HttpServletRequest req,
            Exception exception) {

        var error = build(HttpStatus.GATEWAY_TIMEOUT, exception, req);
        logger.error(error.message);
        return error;
    }


    /**
     * Generic Exception handler translates them to INTERNAL_SERVER_ERROR Http codes
     */
    @ResponseBody
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler({Exception.class, RuntimeException.class})
    public ExceptionDetailDto handleUnhandled(
            HttpServletRequest req,
            Exception exception) throws IOException {

        var error = build(HttpStatus.INTERNAL_SERVER_ERROR, exception, req);
        logger.error(error.message, exception);
        return error;
    }



    private ExceptionDetailDto build(HttpStatus status, Throwable exception, HttpServletRequest req){
        var requestUrl = req.getMethod() + " " + req.getRequestURI();
        return ExceptionDetailDto.build(status.toString(), exception, requestUrl);
    }

}
