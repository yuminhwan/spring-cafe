package com.prgrms.springcafe.product.domain.vo;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import com.prgrms.springcafe.global.error.exception.InvalidValueException;

class PriceTest {

    @DisplayName("정상적인 가격값이라면 Price 객체를 생성한다.")
    @ParameterizedTest
    @ValueSource(longs = {1, 100, 10000})
    void should_ReturnPrice_ValidPrice(long amount) {
        // given
        // when
        Price price = new Price(amount);

        // then
        assertThat(price.getAmount()).isEqualTo(amount);
    }

    @DisplayName("가격은 1미만이면 안된다.")
    @ParameterizedTest
    @ValueSource(longs = {-1, -100, -12, 0})
    void should_ThrowException_PriceIsUnderOne(long price) {
        assertThatThrownBy(() -> new Price(price))
            .isInstanceOf(InvalidValueException.class)
            .hasMessage("가격은 1보다 작을 수 없습니다.");
    }

}
