package com.prgrms.springcafe.global.error.exception;

public enum ErrorCode {
    INVALID_INPUT_VALUE(400, "잘못된 값 입력입니다."),
    ENTITY_NOT_FOUND(404, "엔티티를 찾을 수 없습니다."),

    NAME_DUPLICATION(400, "상품 이름이 중복됩니다.");

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
