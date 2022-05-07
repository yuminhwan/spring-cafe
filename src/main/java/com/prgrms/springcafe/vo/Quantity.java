package com.prgrms.springcafe.vo;

import java.util.Objects;

import com.prgrms.springcafe.global.error.exception.InvalidValueException;

public class Quantity {

    public static final String STOCK_NEGATIVE_EXCEPTION_MESSAGE = "상품 수량은 음수가 될 수 없습니다.";
    public static final int ZERO_AMOUNT = 0;

    private final int amount;

    public Quantity(int amount) {
        this.amount = amount;
        validateStock(amount);
    }

    private void validateStock(int amount) {
        if (amount < ZERO_AMOUNT) {
            throw new InvalidValueException(STOCK_NEGATIVE_EXCEPTION_MESSAGE);
        }
    }

    public Quantity minusQuantity(Quantity quantity) {
        return new Quantity(this.amount - quantity.amount);
    }

    public boolean isUnder(Quantity quantity) {
        return this.amount < quantity.getAmount();
    }

    public int getAmount() {
        return amount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Quantity quantity = (Quantity)o;
        return amount == quantity.amount;
    }

    @Override
    public int hashCode() {
        return Objects.hash(amount);
    }
}
