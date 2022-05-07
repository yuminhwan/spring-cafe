package com.prgrms.springcafe.order.exception;

import java.text.MessageFormat;

import com.prgrms.springcafe.global.error.exception.BusinessException;
import com.prgrms.springcafe.global.error.exception.ErrorCode;

public class OrderCanNotCancelException extends BusinessException {
    public OrderCanNotCancelException(Long id) {
        super(MessageFormat.format("Id가 {0}인 주문은 취소할 수 없습니다.", id), ErrorCode.ORDER_CAN_NOT_CANCEL);
    }
}
