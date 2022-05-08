package com.prgrms.springcafe.order.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import com.prgrms.springcafe.order.domain.Order;
import com.prgrms.springcafe.order.domain.OrderStatus;

public class OrderResponse {

    private final Long id;
    private final String email;
    private final String address;
    private final String postcode;
    private final OrderStatus orderStatus;
    private final List<OrderItemResponse> orderItems;
    private final long totalMoney;
    private final LocalDateTime createdDateTime;
    private final LocalDateTime modifiedDateTime;

    public OrderResponse(Long id, String email, String address, String postcode, OrderStatus orderStatus,
        List<OrderItemResponse> orderItems, long totalMoney, LocalDateTime createdDateTime,
        LocalDateTime modifiedDateTime) {
        this.id = id;
        this.email = email;
        this.address = address;
        this.postcode = postcode;
        this.orderStatus = orderStatus;
        this.orderItems = orderItems;
        this.totalMoney = totalMoney;
        this.createdDateTime = createdDateTime;
        this.modifiedDateTime = modifiedDateTime;
    }

    public static OrderResponse from(Order order) {
        Long id = order.getId();
        String email = order.getOrderer().getEmail().getEmail();
        String address = order.getOrderer().getAddress().getAddress();
        String postcode = order.getOrderer().getAddress().getPostCode();
        OrderStatus orderStatus = order.getOrderStatus();
        List<OrderItemResponse> orderItemResponses = mapToResponse(order);
        long totalMoney = order.getTotalMoney().getAmount();
        LocalDateTime createdDateTime = order.getCreatedDateTime();
        LocalDateTime modifiedDateTime = order.getModifiedDateTime();

        return new OrderResponse(id, email, address, postcode, orderStatus, orderItemResponses, totalMoney,
            createdDateTime, modifiedDateTime);
    }

    private static List<OrderItemResponse> mapToResponse(Order order) {
        return order.getOrderItems().getOrderItems()
            .stream()
            .map(OrderItemResponse::from)
            .collect(Collectors.toList());
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getAddress() {
        return address;
    }

    public String getPostcode() {
        return postcode;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public List<OrderItemResponse> getOrderItems() {
        return orderItems;
    }

    public long getTotalMoney() {
        return totalMoney;
    }

    public LocalDateTime getCreatedDateTime() {
        return createdDateTime;
    }

    public LocalDateTime getModifiedDateTime() {
        return modifiedDateTime;
    }
}
