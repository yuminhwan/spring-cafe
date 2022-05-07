package com.prgrms.springcafe.order.service;

import static com.prgrms.springcafe.order.domain.OrderStatus.*;
import static com.prgrms.springcafe.product.domain.Category.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.prgrms.springcafe.order.domain.Order;
import com.prgrms.springcafe.order.domain.OrderItem;
import com.prgrms.springcafe.order.domain.OrderItems;
import com.prgrms.springcafe.order.domain.Orderer;
import com.prgrms.springcafe.order.domain.vo.Address;
import com.prgrms.springcafe.order.domain.vo.Email;
import com.prgrms.springcafe.order.dto.CreateOrderItemRequest;
import com.prgrms.springcafe.order.dto.CreateOrderRequest;
import com.prgrms.springcafe.order.dto.ModifyAddressRequest;
import com.prgrms.springcafe.order.dto.OrderResponse;
import com.prgrms.springcafe.order.exception.OrderNotFoundException;
import com.prgrms.springcafe.order.exception.ProductOutOfStockExcpetion;
import com.prgrms.springcafe.order.repository.OrderRepository;
import com.prgrms.springcafe.product.domain.Product;
import com.prgrms.springcafe.product.domain.vo.ProductName;
import com.prgrms.springcafe.product.repository.ProductRepostiory;
import com.prgrms.springcafe.vo.Money;
import com.prgrms.springcafe.vo.Quantity;

@ExtendWith(MockitoExtension.class)
class DefaultOrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private ProductRepostiory productRepostiory;

    @InjectMocks
    private DefaultOrderService orderService;

    @DisplayName("주문을 생성한다.")
    @Test
    void createOrder() {
        // given
        CreateOrderRequest orderRequest = createOrderRequest();
        Product americano = products().get(0);
        Product latte = products().get(1);
        given(orderRepository.insert(any(Order.class))).willReturn(order());
        given(productRepostiory.findById(1L)).willReturn(Optional.of(americano));
        given(productRepostiory.findById(2L)).willReturn(Optional.of(latte));

        // when
        OrderResponse orderResponse = orderService.createOrder(orderRequest);

        // then
        assertThat(orderResponse.getId()).isEqualTo(1L);
        then(orderRepository).should(times(1)).insert(any(Order.class));
        then(productRepostiory).should(times(2)).findById(any(Long.class));
        then(productRepostiory).should(times(2)).update(any(Product.class));
    }

    @DisplayName("상품의 재고보다 주문 수량이 더 많을 수 없다.")
    @Test
    void createOrder_QuantityIsOverStock() {
        // given
        CreateOrderRequest orderRequest = createOrderRequest();
        Product americano = products().get(0);
        Product latte = products().get(1);
        americano.sellProduct(new Quantity(99));
        given(productRepostiory.findById(1L)).willReturn(Optional.of(americano));

        // when
        // then
        assertThatThrownBy(() -> orderService.createOrder(orderRequest))
            .isInstanceOf(ProductOutOfStockExcpetion.class)
            .hasMessage("Id가 1인 상품의 수량이 충분하지 않습니다.");
    }

    @DisplayName("주문의 배송지를 변경한다.")
    @Test
    void changeAddress() {
        // given
        Order order = order();
        ModifyAddressRequest addressRequest = modifyAddressRequest();
        given(orderRepository.findById(1L)).willReturn(Optional.of(order));

        // when
        orderService.changeAddress(1L, addressRequest);

        // then
        then(orderRepository).should(times(1)).findById(1L);
        order.changeAddress(addressRequest.toEntity());
        then(orderRepository).should(times(1)).updateAddress(order);
    }

    @DisplayName("저장되지 않은 Id로 주문 배송지를 수정할 수 없다.")
    @Test
    void changeAddress_WithNotSavedId() {
        // given
        Long id = 10L;
        given(orderRepository.findById(id)).willReturn(Optional.empty());

        // when
        // then
        assertThatThrownBy(() -> orderService.changeAddress(10L, modifyAddressRequest()))
            .isInstanceOf(OrderNotFoundException.class)
            .hasMessage("Id가 10인 주문은 존재하지 않습니다.");
    }

    @DisplayName("주문 상태를 변경한다.")
    @Test
    void changeStatus() {
        // given
        Order order = order();
        given(orderRepository.findById(1L)).willReturn(Optional.of(order));

        // when
        orderService.changeStatus(1L, PAYMENT_CONFIRMED);

        // then
        then(orderRepository).should(times(1)).findById(1L);
        then(orderRepository).should(times(1)).updateStatus(any(Order.class));
    }

    @DisplayName("모든 주문을 찾는다.")
    @Test
    void findOrders_All() {
        // given
        List<Order> orders = orders();
        given(orderRepository.findAll()).willReturn(orders);

        // when
        orderService.findOrders(Optional.empty());

        // then
        then(orderRepository).should(times(1)).findAll();
    }

    @DisplayName("주문자의 주문들을 찾는다.")
    @Test
    void findOrders_WithEmail() {
        // given
        List<Order> orders = orders();
        Email email = new Email("hwan@gmail.com");
        given(orderRepository.findByEmail(email)).willReturn(orders);

        // when
        List<OrderResponse> orderResponses = orderService.findOrders(Optional.of(email));

        // then
        then(orderRepository).should(times(1)).findByEmail(email);
    }

    @DisplayName("특정 id의 주문을 찾는다.")
    @Test
    void findOrderById() {
        // given
        Order order = order();
        given(orderRepository.findById(1L)).willReturn(Optional.of(order));

        // when
        orderService.findOrderById(1L);

        // then
        then(orderRepository).should(times(1)).findById(1L);
    }

    private Order order() {
        return new Order(1L, new Orderer(new Email("hwan@gmail.com"), new Address("구미시 옥계동", "12345")), orderItems(),
            ACCEPTED, LocalDateTime.now(), LocalDateTime.now());
    }

    public List<Order> orders() {
        return List.of(
            new Order(1L, new Orderer(new Email("hwan@gmail.com"), new Address("구미시 옥계동", "12345")), orderItems(),
                ACCEPTED, LocalDateTime.now(), LocalDateTime.now()),
            new Order(2L, new Orderer(new Email("hwan@gmail.com"), new Address("구미시 옥계동", "12345")), orderItems(),
                ACCEPTED, LocalDateTime.now(), LocalDateTime.now()));
    }

    private OrderItems orderItems() {
        return new OrderItems(List.of(new OrderItem(1L, 1L, 1L, new Money(3000L), new Quantity(10)),
            new OrderItem(1L, 1L, 2L, new Money(5000L), new Quantity(30))));
    }

    private CreateOrderRequest createOrderRequest() {
        return new CreateOrderRequest("hwan@gmail.com", "경상북도 구미시 흥안로43", "12345", createOrderItemRequests());
    }

    private List<CreateOrderItemRequest> createOrderItemRequests() {
        return List.of(new CreateOrderItemRequest(1L, 3000L, 10),
            new CreateOrderItemRequest(2L, 5000L, 30));
    }

    private List<Product> products() {
        return List.of(
            new Product(1L, new ProductName("americano"), COFFEE, new Money(3000L), new Quantity(100), "americano",
                LocalDateTime.now(), LocalDateTime.now()),
            new Product(2L, new ProductName("latte"), COFFEE, new Money(5000L), new Quantity(100), "latte",
                LocalDateTime.now(), LocalDateTime.now()));
    }

    private ModifyAddressRequest modifyAddressRequest() {
        return new ModifyAddressRequest("서울시 강남구", "345667");
    }
}
