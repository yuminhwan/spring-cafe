package com.prgrms.springcafe.order.exception;

import java.text.MessageFormat;

import com.prgrms.springcafe.global.error.exception.BusinessException;
import com.prgrms.springcafe.global.error.exception.ErrorCode;

public class ProductOutOfStockExcpetion extends BusinessException {
    public ProductOutOfStockExcpetion(Long id) {
        super(MessageFormat.format("Id가 {0}인 상품의 수량이 충분하지 않습니다.", id), ErrorCode.INVALID_INPUT_VALUE);
    }
}
