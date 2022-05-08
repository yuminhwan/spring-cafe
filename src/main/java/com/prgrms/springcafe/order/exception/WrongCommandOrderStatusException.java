package com.prgrms.springcafe.order.exception;

import com.prgrms.springcafe.global.error.exception.ErrorCode;
import com.prgrms.springcafe.global.error.exception.InvalidValueException;

public class WrongCommandOrderStatusException extends InvalidValueException {
    public WrongCommandOrderStatusException() {
        super(ErrorCode.WRONG_COMMAND_ORDER_STATUS.getMessage(), ErrorCode.WRONG_COMMAND_ORDER_STATUS);
    }
}
