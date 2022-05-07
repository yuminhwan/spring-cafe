package com.prgrms.springcafe.order.domain;

import java.time.LocalDateTime;
import java.util.List;

import com.prgrms.springcafe.order.domain.vo.Address;
import com.prgrms.springcafe.order.domain.vo.Email;
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

    public static Order of(String email, String address, String postcode, List<OrderItem> orderItems,
        OrderStatus orderStatus) {
        Orderer orderer = new Orderer(new Email(email), new Address(address, postcode));
        return of(orderer, new OrderItems(orderItems), orderStatus);
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
        this.modifiedDateTime = LocalDateTime.now();
    }

    public void readyForDelivery() {
        this.orderStatus = OrderStatus.READY_FOR_DELIVERY;
        this.modifiedDateTime = LocalDateTime.now();
    }

    public void startDelivery() {
        this.orderStatus = OrderStatus.SHIPPED;
        this.modifiedDateTime = LocalDateTime.now();
    }

    public void completeDelivery() {
        this.orderStatus = OrderStatus.DELIVERED;
        this.modifiedDateTime = LocalDateTime.now();
    }

    public void cancel() {
        this.orderStatus = OrderStatus.CANCELLED;
        this.modifiedDateTime = LocalDateTime.now();
    }

    public boolean isNotModifiable() {
        return this.orderStatus != OrderStatus.ACCEPTED;
    }

    public boolean isNotDeliverable() {
        return orderStatus != OrderStatus.PAYMENT_CONFIRMED;
    }

    public boolean isNotCancelable() {
        return orderStatus != OrderStatus.PAYMENT_CONFIRMED && orderStatus != OrderStatus.ACCEPTED;
    }

    public boolean isNotShippable() {
        return orderStatus != OrderStatus.READY_FOR_DELIVERY;
    }

    public boolean isNotDeliveryCompletable() {
        return orderStatus != OrderStatus.SHIPPED;
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
