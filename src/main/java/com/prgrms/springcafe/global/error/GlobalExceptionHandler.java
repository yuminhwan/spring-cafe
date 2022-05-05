package com.prgrms.springcafe.global.error;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.prgrms.springcafe.global.error.exception.BusinessException;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler({BusinessException.class})
    public void handleEntityNotFoundException(HttpServletResponse response, BusinessException e) throws IOException {
        response.sendError(e.getErrorCode().getStatus(), e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    protected void handleException(HttpServletResponse response, Exception e) throws IOException {
        logger.error(e.getMessage());
        response.sendError(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
    }

}
