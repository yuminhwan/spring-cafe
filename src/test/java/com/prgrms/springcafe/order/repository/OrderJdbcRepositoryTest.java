package com.prgrms.springcafe.order.repository;

import static com.prgrms.springcafe.TestUtils.*;
import static com.prgrms.springcafe.order.domain.OrderStatus.*;
import static org.assertj.core.api.Assertions.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.context.jdbc.Sql;

import com.prgrms.springcafe.order.domain.Order;
import com.prgrms.springcafe.order.domain.vo.Address;
import com.prgrms.springcafe.order.domain.vo.Email;

@JdbcTest
@Sql({"classpath:orderTest.sql"})
class OrderJdbcRepositoryTest {

    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;

    private OrderRepository orderRepository;

    @BeforeEach
    void setup() {
        orderRepository = new OrderJdbcRepository(jdbcTemplate);
    }

    @DisplayName("주문을 저장한다.")
    @Test
    void insert() {
        // given
        Order order = order();

        // when
        Order newOrder = orderRepository.insert(order);

        //then
        assertThat(newOrder.getId()).isEqualTo(1L);
        assertThat(newOrder.getOrderItems().getOrderItems()).extracting("id", "orderId")
            .containsExactly(tuple(1L, 1L), tuple(2L, 1L));
    }

    @DisplayName("주문 배송 주소를 수정한다.")
    @Test
    void updateAddress() {
        // given
        Order order = orderRepository.insert(order());
        Address address = new Address("옥계동 흥안로43", "2345");

        // when
        order.changeAddress(address);
        orderRepository.updateAddress(order);

        // then
        Optional<Order> findOrder = orderRepository.findById(1L);
        assertThat(findOrder).isNotEmpty();
        assertThat(findOrder.get().getOrderer().getAddress()).isEqualTo(address);
        assertThat(findOrder.get().getModifiedDateTime()).isNotEqualTo(order.getModifiedDateTime());
    }

    @DisplayName("주문 상태를 수정한다.")
    @Test
    void updateStatus() {
        // given
        Order order = orderRepository.insert(order());

        // when
        order.readyForDelivery();
        orderRepository.updateStatus(order);

        // then
        Optional<Order> findOrder = orderRepository.findById(1L);
        assertThat(findOrder).isNotEmpty()
            .get().extracting("orderStatus")
            .isEqualTo(READY_FOR_DELIVERY);
    }

    @DisplayName("전체 주문을 조회한다.")
    @Test
    void findAll() {
        // given
        List<Order> orders = orders();
        orders.forEach(orderRepository::insert);

        // when

        // then
        List<Order> findOrders = orderRepository.findAll();
        assertThat(findOrders).hasSize(2)
            .usingRecursiveComparison()
            .ignoringFields("id", "orderItems")
            .isEqualTo(orders);

        assertThat(findOrders).extracting("orderItems").extracting("orderItems")
            .usingRecursiveComparison()
            .ignoringFields("id", "orderId")
            .isEqualTo(List.of(orderItems().getOrderItems(), orderItems().getOrderItems()));
    }

    @DisplayName("특정 이메일의 주문들을 조회한다.")
    @Test
    void findByEmail() {
        // given
        Email email = new Email("hwan@gmail.com");
        orders().forEach(orderRepository::insert);

        // when
        List<Order> findOrder = orderRepository.findByEmail(email);

        // then
        assertThat(findOrder).hasSize(1);
    }

    @DisplayName("특정 id의 주문을 조회한다.")
    @Test
    void findById() {
        // given
        Order order = orderRepository.insert(order());

        // when
        Optional<Order> findOrder = orderRepository.findById(order.getId());

        // then
        assertThat(findOrder).isNotEmpty()
            .get()
            .usingRecursiveComparison()
            .isEqualTo(order);
    }

    @DisplayName("주문을 제거한다.")
    @Test
    void deleteById() {
        // given
        Order order = orderRepository.insert(order());

        // when
        // given
        assertThatCode(() -> orderRepository.deleteById(1L))
            .doesNotThrowAnyException();
    }

}
