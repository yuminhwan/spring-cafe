package com.prgrms.springcafe.order.domain;

import java.util.function.Consumer;

import com.prgrms.springcafe.order.exception.WrongCommandOrderStatusException;

public enum OrderStatus {
    ACCEPTED(order -> {
        throw new WrongCommandOrderStatusException();
    }),
    PAYMENT_CONFIRMED(Order::completePayment),
    READY_FOR_DELIVERY(Order::readyForDelivery),
    SHIPPED(Order::startDelivery),
    SETTLED(Order::completeDelivery),
    CANCELLED(Order::cancelOrder);

    private final Consumer<Order> changeStatus;

    OrderStatus(Consumer<Order> changeStatus) {
        this.changeStatus = changeStatus;
    }

    public void changeStatus(Order order) {
        this.changeStatus.accept(order);
    }
}
