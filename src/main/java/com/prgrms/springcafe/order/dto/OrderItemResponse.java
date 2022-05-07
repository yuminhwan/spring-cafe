package com.prgrms.springcafe.order.dto;

import com.prgrms.springcafe.order.domain.OrderItem;

public class OrderItemResponse {

    private final Long id;
    private final Long orderId;
    private final Long productId;
    private final long price;
    private final int quantity;
    private final long totalPrice;

    public OrderItemResponse(Long id, Long orderId, Long productId, long price, int quantity, long totalPrice) {
        this.id = id;
        this.orderId = orderId;
        this.productId = productId;
        this.price = price;
        this.quantity = quantity;
        this.totalPrice = totalPrice;
    }

    public static OrderItemResponse from(OrderItem orderItem) {
        return new OrderItemResponse(orderItem.getId(), orderItem.getOrderId(), orderItem.getProductId(),
            orderItem.getPrice().getAmount(), orderItem.getQuantity().getAmount(),
            orderItem.getTotalPrice().getAmount());
    }

    public Long getId() {
        return id;
    }

    public Long getOrderId() {
        return orderId;
    }

    public Long getProductId() {
        return productId;
    }

    public long getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

    public long getTotalPrice() {
        return totalPrice;
    }
}
