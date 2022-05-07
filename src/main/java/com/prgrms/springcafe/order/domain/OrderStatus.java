package com.prgrms.springcafe.order.domain;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import com.prgrms.springcafe.order.exception.WrongCommandOrderStatusException;

public enum OrderStatus {
    ACCEPTED(order -> {
        throw new WrongCommandOrderStatusException();
    }),
    PAYMENT_CONFIRMED(order -> {
        if (order.isNotModifiable()) {
            throw new WrongCommandOrderStatusException();
        }
        order.completePayment();
    }),
    READY_FOR_DELIVERY(order -> {
        if (order.isNotDeliverable()) {
            throw new WrongCommandOrderStatusException();
        }

        order.readyForDelivery();
    }),
    SHIPPED(order -> {
        if (order.isNotShippable()) {
            throw new WrongCommandOrderStatusException();
        }
        order.startDelivery();
    }),
    DELIVERED(order -> {
        if (order.isNotDeliveryCompletable()) {
            throw new WrongCommandOrderStatusException();
        }
        order.completeDelivery();
    }),
    CANCELLED(order -> {
        if (order.isNotCancelable()) {
            throw new WrongCommandOrderStatusException();
        }
        order.cancel();
    });

    private final Consumer<Order> changeStatus;

    OrderStatus(Consumer<Order> changeStatus) {
        this.changeStatus = changeStatus;
    }

    public static List<String> toStringAll() {
        return Arrays.stream(OrderStatus.values())
            .map(Objects::toString)
            .collect(Collectors.toList());
    }

    public void changeStatus(Order order) {
        this.changeStatus.accept(order);
    }
}
