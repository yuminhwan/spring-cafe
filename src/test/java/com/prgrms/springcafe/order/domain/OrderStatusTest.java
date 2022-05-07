package com.prgrms.springcafe.order.domain;

import static com.prgrms.springcafe.TestUtils.*;
import static com.prgrms.springcafe.order.domain.OrderStatus.*;
import static org.assertj.core.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import com.prgrms.springcafe.order.exception.WrongCommandOrderStatusException;

class OrderStatusTest {

    private static Stream<Arguments> provideOrder() {
        LocalDateTime now = LocalDateTime.now();
        return Stream.of(
            Arguments.of(new Order(1L, orderer(), orderItems(), ACCEPTED, now, now), PAYMENT_CONFIRMED),
            Arguments.of(new Order(1L, orderer(), orderItems(), READY_FOR_DELIVERY, now, now), SHIPPED),
            Arguments.of(new Order(1L, orderer(), orderItems(), SHIPPED, now, now), DELIVERED),
            Arguments.of(new Order(1L, orderer(), orderItems(), ACCEPTED, now, now), CANCELLED));
    }

    private static Stream<Arguments> provideWrongOrder() {
        LocalDateTime now = LocalDateTime.now();
        return Stream.of(
            Arguments.of(new Order(1L, orderer(), orderItems(), PAYMENT_CONFIRMED, now, now), ACCEPTED),
            Arguments.of(new Order(1L, orderer(), orderItems(), SHIPPED, now, now), READY_FOR_DELIVERY),
            Arguments.of(new Order(1L, orderer(), orderItems(), DELIVERED, now, now), CANCELLED),
            Arguments.of(new Order(1L, orderer(), orderItems(), CANCELLED, now, now), PAYMENT_CONFIRMED));
    }

    @DisplayName("주문 상태를 변경한다.")
    @ParameterizedTest
    @MethodSource("provideOrder")
    void changeStatus(Order order, OrderStatus orderStatus) {
        // given
        // when
        orderStatus.changeStatus(order);

        // then
        assertThat(order.getOrderStatus()).isEqualTo(orderStatus);
    }

    @DisplayName("주문 상태가 전 단계가 아니거나 ACCEPTED라면 변경할 수 없다.")
    @ParameterizedTest
    @MethodSource("provideWrongOrder")
    void changeStatus_WithACCEPTED(Order order, OrderStatus orderStatus) {

        assertThatThrownBy(() -> orderStatus.changeStatus(order))
            .isInstanceOf(WrongCommandOrderStatusException.class)
            .hasMessage("잘못된 주문 상태 변경입니다.");
    }
}
