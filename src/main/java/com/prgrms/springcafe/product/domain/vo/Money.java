package com.prgrms.springcafe.product.domain.vo;

import java.util.Objects;

import com.prgrms.springcafe.global.error.exception.InvalidValueException;

public class Money {
    public static final int MIN_AMOUNT = 1;
    public static final String PRICE_UNDER_MIN_PRICE_EXCEPTION_MESSAGE = "돈은 1보다 작을 수 없습니다.";

    private final long amount;

    public Money(long amount) {
        this.amount = amount;
        validatePrice(amount);
    }

    private void validatePrice(long amount) {
        if (amount < MIN_AMOUNT) {
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
        Money money = (Money)o;
        return amount == money.amount;
    }

    @Override
    public int hashCode() {
        return Objects.hash(amount);
    }
}
