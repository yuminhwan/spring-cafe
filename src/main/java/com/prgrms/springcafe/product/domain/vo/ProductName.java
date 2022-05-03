package com.prgrms.springcafe.product.domain.vo;

import java.util.Objects;

import com.prgrms.springcafe.global.error.exception.InvalidValueException;

public class ProductName {

    private static final int MIN_LENGTH = 3;
    private static final int MAX_LENGTH = 20;
    private static final String NAME_EMPTY_OR_BLANK_EXCEPTION_MESSAGE = "상품 이름은 반드시 존재해야 합니다.";
    private static final String WRONG_NAME_LENGTH_EXCEPTION_MESSAGE = "상품 이름은 3글자 이상 20글자 이하여야합니다.";

    private final String value;

    public ProductName(String value) {
        this.value = value;
        validateName(value);
    }

    private void validateName(String value) {
        if (Objects.isNull(value) || value.isBlank()) {
            throw new InvalidValueException(NAME_EMPTY_OR_BLANK_EXCEPTION_MESSAGE);
        }

        if (value.length() > MAX_LENGTH || value.length() < MIN_LENGTH) {
            throw new InvalidValueException(WRONG_NAME_LENGTH_EXCEPTION_MESSAGE);
        }
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        ProductName productName = (ProductName)o;
        return value.equals(productName.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
