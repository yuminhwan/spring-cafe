package com.prgrms.springcafe.order.exception;

import java.text.MessageFormat;

import com.prgrms.springcafe.global.error.exception.EntityNotFoundException;

public class OrderItemNotFoundException extends EntityNotFoundException {
    public OrderItemNotFoundException(Long id) {
        super(MessageFormat.format("Id가 {0}인 주문제품은 존재하지 않습니다.", id));
    }
}
