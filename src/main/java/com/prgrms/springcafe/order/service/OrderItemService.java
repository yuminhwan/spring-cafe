package com.prgrms.springcafe.order.service;

import com.prgrms.springcafe.vo.Quantity;

public interface OrderItemService {

    void changeOrderItem(Long id, Quantity quantity);

    void removeOrderItem(Long id);

}
