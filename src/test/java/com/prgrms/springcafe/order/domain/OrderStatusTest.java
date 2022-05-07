package com.prgrms.springcafe.order.domain;

import static com.prgrms.springcafe.TestUtils.*;
import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import com.prgrms.springcafe.order.exception.WrongCommandOrderStatusException;

class OrderStatusTest {

    @DisplayName("주문 상태를 변경한다.")
    @ParameterizedTest
    @CsvSource(value = {"PAYMENT_CONFIRMED", "READY_FOR_DELIVERY", "SHIPPED", "SETTLED", "CANCELLED"})
    void changeStatus(OrderStatus orderStatus) {
        // given
        Order order = order();

        // when
        orderStatus.changeStatus(order);

        // then
        assertThat(order.getOrderStatus()).isEqualTo(orderStatus);
    }

    @DisplayName("ACCEPTED로 변경할 수 없다.")
    @Test
    void changeStatus_WithACCEPTED() {
        // given
        Order order = order();

        // when
        // then
        assertThatThrownBy(() -> OrderStatus.ACCEPTED.changeStatus(order))
            .isInstanceOf(WrongCommandOrderStatusException.class)
            .hasMessage("ACCEPTED로 변경할 수 없습니다.");
    }
}
