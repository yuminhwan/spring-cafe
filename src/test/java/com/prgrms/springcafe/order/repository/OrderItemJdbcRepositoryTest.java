package com.prgrms.springcafe.order.repository;

import static org.assertj.core.api.Assertions.*;

import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.context.jdbc.Sql;

import com.prgrms.springcafe.order.domain.OrderItem;
import com.prgrms.springcafe.vo.Money;
import com.prgrms.springcafe.vo.Quantity;

@JdbcTest
@Sql({"classpath:orderItemTest.sql"})
class OrderItemJdbcRepositoryTest {

    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;

    private OrderItemRepository orderItemRepository;

    @BeforeEach
    void setup() {
        orderItemRepository = new OrderItemJdbcRepository(jdbcTemplate);
    }

    @DisplayName("주문 상품의 수량을 수정한다.")
    @Test
    void update() {
        // given
        OrderItem orderItem = orderItem();
        orderItem.changeQuantity(new Quantity(10));

        // when
        orderItemRepository.update(orderItem);

        // then
        Optional<OrderItem> findOrderItem = orderItemRepository.findById(1L);
        Assertions.assertThat(findOrderItem).isNotEmpty()
            .get()
            .usingRecursiveComparison()
            .isEqualTo(orderItem);
    }

    @DisplayName("특정 id의 주문 상품을 조회한다.")
    @Test
    void findById() {
        // given
        OrderItem orderItem = orderItem();

        // when
        Optional<OrderItem> findOrderItem = orderItemRepository.findById(1L);

        // then
        Assertions.assertThat(findOrderItem).isNotEmpty()
            .get()
            .usingRecursiveComparison()
            .isEqualTo(orderItem);
    }

    @DisplayName("주문 상품을 제거한다.")
    @Test
    void deleteById() {
        // given
        OrderItem orderItem = orderItem();

        // when
        // then
        assertThatCode(() -> orderItemRepository.deleteById(1L))
            .doesNotThrowAnyException();
    }

    private OrderItem orderItem() {
        return new OrderItem(1L, 1L, 1L, new Money(2500), new Quantity(4));
    }
}
