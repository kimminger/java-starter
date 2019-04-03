package com.elderbyte.spring.bootstrap.servlet;

import com.elderbyte.commons.exceptions.ExceptionUtil;
import com.elderbyte.commons.exceptions.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
    @ExceptionHandler(value = NotFoundException.class)
    public void handleNotFound(
            HttpServletRequest req,
            HttpServletResponse reponse,
            Exception exception) throws IOException {

        String agrMessages = ExceptionUtil.aggregateMessages(exception);

        logger.warn("NotFoundException: " + agrMessages + " resource: " +  req.getRequestURL());

        reponse.sendError(HttpStatus.NOT_FOUND.value(), agrMessages);
    }

    @ExceptionHandler(value = {SocketTimeoutException.class})
    public void timeoutException(
            HttpServletRequest req,
            HttpServletResponse reponse,
            Exception exception) throws IOException {

        String agrMessages = ExceptionUtil.aggregateMessages(exception);

        var message = exception.getClass().getSimpleName() + ": " + agrMessages + " resource: " +  req.getRequestURL();

        logger.error(message, exception);

        reponse.sendError(HttpStatus.GATEWAY_TIMEOUT.value(), HttpStatus.GATEWAY_TIMEOUT.value() + " - " + message);
    }


    /**
     * Generic Exception handler translates them to INTERNAL_SERVER_ERROR Http codes
     */
    @ExceptionHandler(value = {Exception.class, RuntimeException.class})
    public void handleUnhandled(
            HttpServletRequest req,
            HttpServletResponse reponse,
            Exception exception) throws IOException {

        String agrMessages = ExceptionUtil.aggregateMessages(exception);

        var message = exception.getClass().getSimpleName() + ": " + agrMessages + " resource: " +  req.getRequestURL();

        logger.error(message, exception);

        reponse.sendError(HttpStatus.INTERNAL_SERVER_ERROR.value(), HttpStatus.INTERNAL_SERVER_ERROR.value() + " - " + message);
    }

}
