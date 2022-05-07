package com.prgrms.springcafe.order.domain;

import java.util.ArrayList;
import java.util.List;

import com.prgrms.springcafe.global.error.exception.InvalidValueException;
import com.prgrms.springcafe.vo.Money;

public class OrderItems {
    private final List<OrderItem> orderItems;

    public OrderItems(List<OrderItem> orderItems) {
        this.orderItems = new ArrayList<>(orderItems);
    }

    public Money calculateTotalMoney() {
        return orderItems.stream()
            .map(OrderItem::getTotalPrice)
            .reduce(Money::add)
            .orElseThrow(() -> new InvalidValueException("계산할 수 없는 알 수 없는 값입니다."));
    }

    public List<OrderItem> getOrderItems() {
        return List.copyOf(orderItems);
    }
}
