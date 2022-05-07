package com.prgrms.springcafe.global.error.exception;

public enum ErrorCode {

    //Common
    INVALID_INPUT_VALUE(400, "잘못된 값 입력입니다."),
    ENTITY_NOT_FOUND(404, "엔티티를 찾을 수 없습니다."),

    // Product
    NAME_DUPLICATION(400, "상품 이름이 중복됩니다."),

    // Order
    ORDER_CAN_NOT_CANCEL(400, "해당 주문은 취소할 수 없습니다."),
    WRONG_COMMAND_ORDER_STATUS(400, "잘못된 주문 상태 변경입니다.");

    private final int status;
    private final String message;

    ErrorCode(int status, String message) {
        this.status = status;
        this.message = message;
    }

    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }
}
