package com.prgrms.springcafe.global.error;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.prgrms.springcafe.global.error.exception.BusinessException;
import com.prgrms.springcafe.order.controller.OrderController;
import com.prgrms.springcafe.product.controller.ProductController;

@ControllerAdvice(assignableTypes = {ProductController.class, OrderController.class})
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler({BusinessException.class})
    public void handleEntityNotFoundException(HttpServletResponse response, BusinessException e) throws IOException {
        response.sendError(e.getErrorCode().getStatus(), e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public void handleException(HttpServletResponse response, Exception e) throws IOException {
        logger.error(e.getMessage());
        response.sendError(HttpStatus.INTERNAL_SERVER_ERROR.value(), "서버에서 오류가 발생했습니다.");
    }

}
