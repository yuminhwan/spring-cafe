package com.prgrms.springcafe.order.repository;

import java.util.List;
import java.util.Optional;

import com.prgrms.springcafe.order.domain.Order;
import com.prgrms.springcafe.order.domain.vo.Email;

public interface OrderRepository {

    Order insert(Order order);

    void updateAddress(Order order);

    List<Order> findAll();

    List<Order> findByEmail(Email email);

    Optional<Order> findById(Long id);

    void deleteById(Long id);

    void updateStatus(Order order);
}
