package com.prgrms.springcafe.product.domain.vo;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import com.prgrms.springcafe.global.error.exception.InvalidValueException;

class StockTest {

    @DisplayName("정상적인 재고값이라면 Stock 객체를 생성한다.")
    @ParameterizedTest
    @ValueSource(ints = {0, 100, 5})
    void should_ReturnStock_ValidStock(int value) {
        // given
        // when
        Stock stock = new Stock(value);

        // then
        assertThat(stock.getAmount()).isEqualTo(value);
    }

    @DisplayName("재고가 음수면 안된다.")
    @ParameterizedTest
    @ValueSource(ints = {-1, -100, -10000})
    void should_ReturnException_StockIsNegative(int stock) {
        assertThatThrownBy(() -> new Stock(stock))
            .isInstanceOf(InvalidValueException.class)
            .hasMessage("상품 수량은 음수가 될 수 없습니다.");
    }

}