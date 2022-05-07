package com.prgrms.springcafe.order.domain;

import java.time.LocalDateTime;

import com.prgrms.springcafe.order.domain.vo.Address;
import com.prgrms.springcafe.vo.Money;

public class Order {
    private final Long id;
    private final OrderItems orderItems;
    private final LocalDateTime createdDateTime;
    private final Money totalMoney;
    private OrderStatus orderStatus;
    private LocalDateTime modifiedDateTime;
    private Orderer orderer;

    public Order(Long id, Orderer orderer, OrderItems orderItems, OrderStatus orderStatus,
        LocalDateTime createdDateTime, LocalDateTime modifiedDateTime) {
        this.id = id;
        this.orderer = orderer;
        this.orderItems = orderItems;
        this.orderStatus = orderStatus;
        this.totalMoney = orderItems.calculateTotalMoney();
        this.createdDateTime = createdDateTime;
        this.modifiedDateTime = modifiedDateTime;
    }

    public static Order of(Orderer orderer, OrderItems orderItems, OrderStatus orderStatus) {
        LocalDateTime now = LocalDateTime.now();
        return new Order(null, orderer, orderItems, orderStatus, now, now);
    }

    public void changeAddress(Address address) {
        this.orderer = orderer.changeAddress(address);
        this.modifiedDateTime = LocalDateTime.now();
    }

    public void completePayment() {
        this.orderStatus = OrderStatus.PAYMENT_CONFIRMED;
    }

    public void readyForDelivery() {
        this.orderStatus = OrderStatus.READY_FOR_DELIVERY;
    }

    public void startDelivery() {
        this.orderStatus = OrderStatus.SHIPPED;
    }

    public void completeDelivery() {
        this.orderStatus = OrderStatus.SETTLED;
    }

    public void cancleOrder() {
        this.orderStatus = OrderStatus.CANCELLED;
    }

    public Long getId() {
        return id;
    }

    public Orderer getOrderer() {
        return orderer;
    }

    public OrderItems getOrderItems() {
        return orderItems;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public Money getTotalMoney() {
        return totalMoney;
    }

    public LocalDateTime getCreatedDateTime() {
        return createdDateTime;
    }

    public LocalDateTime getModifiedDateTime() {
        return modifiedDateTime;
    }
}
