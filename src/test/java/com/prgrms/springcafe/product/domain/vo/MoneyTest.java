package com.prgrms.springcafe.product.domain.vo;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import com.prgrms.springcafe.global.error.exception.InvalidValueException;

class MoneyTest {

    @DisplayName("정상적인 가격값이라면 Price 객체를 생성한다.")
    @ParameterizedTest
    @ValueSource(longs = {1, 100, 10000})
    void should_ReturnMoney_ValidMoney(long amount) {
        // given
        // when
        Money money = new Money(amount);

        // then
        assertThat(money.getAmount()).isEqualTo(amount);
    }

    @DisplayName("가격은 1미만이면 안된다.")
    @ParameterizedTest
    @ValueSource(longs = {-1, -100, -12, 0})
    void should_ThrowException_MoneyIsUnderOne(long money) {
        assertThatThrownBy(() -> new Money(money))
            .isInstanceOf(InvalidValueException.class)
            .hasMessage("돈은 1보다 작을 수 없습니다.");
    }

}
