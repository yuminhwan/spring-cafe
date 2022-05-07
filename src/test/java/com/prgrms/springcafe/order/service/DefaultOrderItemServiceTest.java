package com.prgrms.springcafe.order.service;

import static com.prgrms.springcafe.TestUtils.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.prgrms.springcafe.order.domain.OrderItem;
import com.prgrms.springcafe.order.exception.OrderItemNotFoundException;
import com.prgrms.springcafe.order.repository.OrderItemRepository;
import com.prgrms.springcafe.vo.Quantity;

@ExtendWith(MockitoExtension.class)
class DefaultOrderItemServiceTest {

    @Mock
    private OrderItemRepository orderItemRepository;

    @InjectMocks
    private DefaultOrderItemService orderItemService;

    @DisplayName("주문 상품의 수량을 변경한다.")
    @Test
    void changeOrderItem() {
        // given
        OrderItem orderItem = orderItem();
        given(orderItemRepository.findById(1L)).willReturn(Optional.of(orderItem));

        // when
        orderItemService.changeOrderItem(1L, new Quantity(100));

        // then
        then(orderItemRepository).should(times(1)).findById(1L);
        then(orderItemRepository).should(times(1)).update(orderItem);
    }

    @DisplayName("저장되지 않은 Id로 주문상품을 수정할 수 없다.")
    @Test
    void changeOrderItem_WithNotSavedId() {
        // given
        Long id = 10L;
        given(orderItemRepository.findById(id)).willReturn(Optional.empty());

        // when
        // then
        assertThatThrownBy(() -> orderItemService.changeOrderItem(10L, new Quantity(10)))
            .isInstanceOf(OrderItemNotFoundException.class)
            .hasMessage("Id가 10인 주문제품은 존재하지 않습니다.");
    }

    @DisplayName("주문 상품을 제거한다.")
    @Test
    void removeOrderItem() {
        // given
        OrderItem orderItem = orderItem();

        // when
        orderItemService.removeOrderItem(1L);

        // then
        then(orderItemRepository).should(times(1)).deleteById(1L);
    }
}
