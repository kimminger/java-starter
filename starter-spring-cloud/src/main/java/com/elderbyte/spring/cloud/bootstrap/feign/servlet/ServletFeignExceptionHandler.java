package com.elderbyte.spring.cloud.bootstrap.feign.servlet;

import com.elderbyte.commons.exceptions.ExceptionUtil;
import feign.FeignException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


/**
 * A controller advice which deals with unhandled exceptions and transforms them into
 * HTTP responses.
 */
@Order(1000000)
@ControllerAdvice
public class ServletFeignExceptionHandler {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @ExceptionHandler(value = {FeignException.class})
    public void gatewayException(
            HttpServletRequest req,
            HttpServletResponse reponse,
            Exception exception) throws IOException {

        var message = exception.getClass().getSimpleName() + ": " + ExceptionUtil.aggregateMessages(exception) + " resource: " +  req.getRequestURL();

        logger.error(message, exception);

        reponse.sendError(HttpStatus.BAD_GATEWAY.value(), HttpStatus.BAD_GATEWAY.value() + " - " + message);
    }

}
