package com.prgrms.springcafe.order.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.prgrms.springcafe.order.domain.OrderItem;
import com.prgrms.springcafe.order.exception.OrderItemNotFoundException;
import com.prgrms.springcafe.order.repository.OrderItemRepository;
import com.prgrms.springcafe.vo.Quantity;

@Service
@Transactional
public class DefaultOrderItemService implements OrderItemService {

    private final OrderItemRepository orderItemRepository;

    public DefaultOrderItemService(OrderItemRepository orderItemRepository) {
        this.orderItemRepository = orderItemRepository;
    }

    public void changeOrderItem(Long id, Quantity quantity) {
        OrderItem orderItem = orderItemRepository.findById(id)
            .orElseThrow(() -> new OrderItemNotFoundException(id));

        orderItem.changeQuantity(quantity);
        orderItemRepository.update(orderItem);
    }

    public void removeOrderItem(Long id) {
        orderItemRepository.deleteById(id);
    }
}
