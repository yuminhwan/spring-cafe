package com.prgrms.springcafe;

import static com.prgrms.springcafe.order.domain.OrderStatus.*;

import java.util.List;

import com.prgrms.springcafe.order.domain.Order;
import com.prgrms.springcafe.order.domain.OrderItem;
import com.prgrms.springcafe.order.domain.OrderItems;
import com.prgrms.springcafe.order.domain.Orderer;
import com.prgrms.springcafe.order.domain.vo.Address;
import com.prgrms.springcafe.order.domain.vo.Email;
import com.prgrms.springcafe.vo.Money;
import com.prgrms.springcafe.vo.Quantity;

public class TestUtils {

    public static Order order() {
        return Order.of(orderer(), orderItems(), ACCEPTED);
    }

    public static Orderer orderer() {
        return new Orderer(new Email("hwan@gmail.com"), new Address("구미시 옥계동", "12345"));
    }

    public static List<Order> orders() {
        return List.of(
            Order.of(new Orderer(new Email("hwan@gmail.com"), new Address("구미시 옥계동", "12345")), orderItems(), ACCEPTED),
            Order.of(new Orderer(new Email("armand@gmail.com"), new Address("구미시 옥계동", "12345")), orderItems(),
                ACCEPTED));
    }

    public static OrderItems orderItems() {
        return new OrderItems(List.of(new OrderItem(1L, new Money(1000L), new Quantity(3)),
            new OrderItem(2L, new Money(2000L), new Quantity(5))));
    }

    public static OrderItem orderItem() {
        return new OrderItem(1L, 1L, 1L, new Money(2500L), new Quantity(4));
    }

}
