package com.prgrms.springcafe.product.exception;

import com.prgrms.springcafe.global.error.exception.ErrorCode;
import com.prgrms.springcafe.global.error.exception.InvalidValueException;

public class NameDuplicateException extends InvalidValueException {
    public NameDuplicateException(String message) {
        super(message, ErrorCode.NAME_DUPLICATION);
    }
}
