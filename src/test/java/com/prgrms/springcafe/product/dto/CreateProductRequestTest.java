package com.prgrms.springcafe.product.dto;

import static com.prgrms.springcafe.product.domain.Category.*;
import static org.assertj.core.api.Assertions.*;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

class CreateProductRequestTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @DisplayName("상품 이름 입력값이 빈 값, null, 글자수는 2자 미만이거나 20자 초과하면 안 된다.")
    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"아", "가나다라마바사아자차카타파하가나다라마바마"})
    void validateName(String name) {
        // given
        CreateProductRequest productRequest = new CreateProductRequest(name, CAKE, 100L, 100, "cakeDelicious");

        // when
        // then
        validateResult(productRequest);
    }

    @DisplayName("카테고리 입력값이 null이면 안 된다.")
    @Test
    void validateCategory() {
        // given
        CreateProductRequest productRequest = new CreateProductRequest("cake", null, 100L, 100, "cakeDelicious");

        // when
        // then
        validateResult(productRequest);
    }

    @DisplayName("상품 가격값이 음수나 0이면 안 된다.")
    @ParameterizedTest
    @ValueSource(longs = {-1, 0, -100})
    void validatePrice(long price) {
        // given
        CreateProductRequest productRequest = new CreateProductRequest("cake", CAKE, price, 100, "cakeDelicious");

        // when
        // then
        validateResult(productRequest);
    }

    @DisplayName("상품 재고값이 음수나 0이면 안 된다.")
    @ParameterizedTest
    @ValueSource(ints = {-1, 0, -100})
    void validateStock(int stock) {
        // given
        CreateProductRequest productRequest = new CreateProductRequest("cake", CAKE, 100L, stock, "cakeDelicious");

        // when
        // then
        validateResult(productRequest);
    }

    @DisplayName("상품 설명값이 빈 값, null, 글자수는 5자 미만이거나 100자 초과하면 된다.")
    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"케이크"})
    void validateDescription(String description) {
        // given
        CreateProductRequest productRequest = new CreateProductRequest("cake", CAKE, 100L, 100, description);

        // when
        // then
        validateResult(productRequest);
    }

    private void validateResult(CreateProductRequest productRequest) {
        Set<ConstraintViolation<CreateProductRequest>> violations = validator.validate(productRequest);

        assertThat(violations).isNotEmpty();
    }
}
