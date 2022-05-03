package com.prgrms.springcafe.product.domain.vo;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import com.prgrms.springcafe.global.error.exception.InvalidValueException;

class QuantityTest {

    @DisplayName("정상적인 재고값이라면 Stock 객체를 생성한다.")
    @ParameterizedTest
    @ValueSource(ints = {0, 100, 5})
    void should_ReturnQuantity_ValidQuantity(int value) {
        // given
        // when
        Quantity quantity = new Quantity(value);

        // then
        assertThat(quantity.getAmount()).isEqualTo(value);
    }

    @DisplayName("재고가 음수면 안된다.")
    @ParameterizedTest
    @ValueSource(ints = {-1, -100, -10000})
    void should_ReturnException_QuantityIsNegative(int stock) {
        assertThatThrownBy(() -> new Quantity(stock))
            .isInstanceOf(InvalidValueException.class)
            .hasMessage("상품 수량은 음수가 될 수 없습니다.");
    }

}
