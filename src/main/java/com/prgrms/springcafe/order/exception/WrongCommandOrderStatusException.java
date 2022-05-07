package com.prgrms.springcafe.order.exception;

import com.prgrms.springcafe.global.error.exception.BusinessException;
import com.prgrms.springcafe.global.error.exception.ErrorCode;

public class WrongCommandOrderStatusException extends BusinessException {
    public WrongCommandOrderStatusException() {
        super(ErrorCode.WRONG_COMMAND_ORDER_STATUS);
    }
}
