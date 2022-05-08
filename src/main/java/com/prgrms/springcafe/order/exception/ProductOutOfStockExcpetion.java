package com.prgrms.springcafe.order.exception;

import java.text.MessageFormat;

import com.prgrms.springcafe.global.error.exception.ErrorCode;
import com.prgrms.springcafe.global.error.exception.InvalidValueException;

public class ProductOutOfStockExcpetion extends InvalidValueException {
    public ProductOutOfStockExcpetion(Long id) {
        super(MessageFormat.format("Id가 {0}인 상품의 수량이 충분하지 않습니다.", id), ErrorCode.INVALID_INPUT_VALUE);
    }
}
