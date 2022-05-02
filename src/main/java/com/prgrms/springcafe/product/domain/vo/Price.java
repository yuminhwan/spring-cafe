package com.prgrms.springcafe.product.domain.vo;

import java.util.Objects;

import com.prgrms.springcafe.global.error.exception.InvalidValueException;

public class Price {
    public static final int MIN_PRICE = 1;
    public static final String PRICE_UNDER_MIN_PRICE_EXCEPTION_MESSAGE = "가격은 1보다 작을 수 없습니다.";

    private final long amount;

    public Price(long amount) {
        this.amount = amount;
        validatePrice(amount);
    }

    private void validatePrice(long amount) {
        if (amount < MIN_PRICE) {
            throw new InvalidValueException(PRICE_UNDER_MIN_PRICE_EXCEPTION_MESSAGE);
        }
    }

    public long getAmount() {
        return amount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Price price = (Price)o;
        return amount == price.amount;
    }

    @Override
    public int hashCode() {
        return Objects.hash(amount);
    }
}
