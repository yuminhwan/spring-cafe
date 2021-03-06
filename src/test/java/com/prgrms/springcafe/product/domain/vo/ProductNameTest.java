package com.prgrms.springcafe.product.domain.vo;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import com.prgrms.springcafe.global.error.exception.InvalidValueException;

class ProductNameTest {

    @DisplayName("정상적인 이름값이라면 ProductName 객체를 생성한다.")
    @ParameterizedTest
    @ValueSource(strings = {"나다라", "가나다라마바사아자차카타파하가나다라마바"})
    void should_ReturnProductName_ValidValue(String value) {
        // given
        // when
        ProductName productName = new ProductName(value);

        // then
        assertThat(productName.getValue()).isEqualTo(value);
    }

    @DisplayName("이름은 2자 미만이거나 20자 초과면 안된다.")
    @ParameterizedTest
    @ValueSource(strings = {"아", "가나다라마바사아자차카타파하가나다라마바마"})
    void should_ThrowException_ProductNameLengthOver20(String name) {
        assertThatThrownBy(() -> new ProductName(name))
            .isInstanceOf(InvalidValueException.class)
            .hasMessage("상품 이름은 3글자 이상 20글자 이하여야합니다.");
    }

    @DisplayName("이름은 null이거나 비어있으면 안된다.")
    @ParameterizedTest
    @NullAndEmptySource
    void should_ThrowException_ProductNameIsNullOrBlank(String name) {
        assertThatThrownBy(() -> new ProductName(name))
            .isInstanceOf(InvalidValueException.class)
            .hasMessage("상품 이름은 반드시 존재해야 합니다.");
    }

}
