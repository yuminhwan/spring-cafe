package com.prgrms.springcafe.order.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.prgrms.springcafe.global.error.exception.InvalidValueException;
import com.prgrms.springcafe.vo.Money;
import com.prgrms.springcafe.vo.Quantity;

public class OrderItems {
    private final List<OrderItem> orderItems;

    public OrderItems(List<OrderItem> orderItems) {
        validateOrderItems(orderItems);
        this.orderItems = new ArrayList<>(orderItems);
    }

    private void validateOrderItems(List<OrderItem> orderItems) {
        if (orderItems.isEmpty()) {
            throw new InvalidValueException("주문 상품은 비어있을 수 없습니다.");
        }
    }

    public Money calculateTotalMoney() {
        return orderItems.stream()
            .map(OrderItem::getTotalPrice)
            .reduce(Money::add)
            .orElseThrow(() -> new InvalidValueException("계산할 수 없는 알 수 없는 값입니다."));
    }

    public Map<Long, Quantity> mapToProductAndQuantity() {
        return orderItems.stream()
            .collect(Collectors.toMap(OrderItem::getProductId, OrderItem::getQuantity));
    }

    public List<OrderItem> getOrderItems() {
        return List.copyOf(orderItems);
    }
}
