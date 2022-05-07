package com.prgrms.springcafe.order.repository;

import java.util.Optional;

import com.prgrms.springcafe.order.domain.OrderItem;

public interface OrderItemRepository {

    void update(OrderItem orderItem);

    Optional<OrderItem> findById(Long id);

    void deleteById(Long id);
}
