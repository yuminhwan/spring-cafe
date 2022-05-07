package com.prgrms.springcafe.order.domain;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.prgrms.springcafe.vo.Money;
import com.prgrms.springcafe.vo.Quantity;

class OrderItemsTest {

    @DisplayName("주문 아이템에 대한 총 가격을 계산한다.")
    @Test
    void calculateTotalMoney() {
        // given
        OrderItems orderItems = new OrderItems(List.of(new OrderItem(1L, 1L, 1L, new Money(1000L), new Quantity(3)),
            new OrderItem(2L, 1L, 2L, new Money(2000L), new Quantity(5))));

        // when
        Money totalMoney = orderItems.calculateTotalMoney();

        // then
        assertThat(totalMoney.getAmount()).isEqualTo(13000L);
    }
}
