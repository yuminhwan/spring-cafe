package com.prgrms.springcafe.order.service;

import java.util.List;
import java.util.Optional;

import com.prgrms.springcafe.order.domain.OrderStatus;
import com.prgrms.springcafe.order.domain.vo.Email;
import com.prgrms.springcafe.order.dto.CreateOrderRequest;
import com.prgrms.springcafe.order.dto.ModifyAddressRequest;
import com.prgrms.springcafe.order.dto.OrderResponse;
import com.prgrms.springcafe.vo.Quantity;

public interface OrderService {

    OrderResponse createOrder(CreateOrderRequest createOrderRequest);

    void changeAddress(Long id, ModifyAddressRequest addressRequest);

    void changeOrderItem(Long orderId, Long orderItemId, Quantity quantity);

    List<OrderResponse> findOrders(Optional<Email> email);

    OrderResponse findOrderById(Long id);

    void removeOrderItem(Long orderId, Long orderItemId);

    void changeStatus(Long id, OrderStatus orderStatus);
}
