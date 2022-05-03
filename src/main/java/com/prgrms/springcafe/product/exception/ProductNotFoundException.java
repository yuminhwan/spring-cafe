package com.prgrms.springcafe.product.exception;

import java.text.MessageFormat;

import com.prgrms.springcafe.global.error.exception.EntityNotFoundException;

public class ProductNotFoundException extends EntityNotFoundException {
    public ProductNotFoundException(Long id) {
        super(MessageFormat.format("Id가 {0}인 상품은 존재하지 않습니다.", id));
    }
}
