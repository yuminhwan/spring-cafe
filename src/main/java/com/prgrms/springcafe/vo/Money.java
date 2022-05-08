package com.prgrms.springcafe.vo;

import java.util.Objects;

import com.prgrms.springcafe.global.error.exception.InvalidValueException;

public class Money {
    public static final int MIN_AMOUNT = 0;
    public static final String PRICE_UNDER_MIN_PRICE_EXCEPTION_MESSAGE = "돈은 0보다 작을 수 없습니다.";

    private final long amount;

    public Money(long amount) {
        this.amount = amount;
        validateAmount(amount);
    }

    private void validateAmount(long amount) {
        if (amount < MIN_AMOUNT) {
            throw new InvalidValueException(PRICE_UNDER_MIN_PRICE_EXCEPTION_MESSAGE);
        }
    }

    public Money add(Money money) {
        return new Money(this.amount + money.amount);
    }

    public Money calculateTotal(Quantity quantity) {
        return new Money(this.amount * quantity.getAmount());
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
