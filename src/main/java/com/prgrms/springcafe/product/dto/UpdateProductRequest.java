package com.prgrms.springcafe.product.dto;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

import org.hibernate.validator.constraints.Length;

public class UpdateProductRequest {

    @Positive(message = "상품 가격은 양수여야합니다.")
    private final long price;

    @PositiveOrZero(message = "상품 수량은 0 또는 양수여야합니다.")
    private final int stock;

    @Length(min = 5, max = 100, message = "상품 설명은 {min}글자 이상 {max}글자 이하여야합니다.")
    private final String description;

    public UpdateProductRequest(long price, int stock, String description) {
        this.price = price;
        this.stock = stock;
        this.description = description;
    }

    public long getPrice() {
        return price;
    }

    public int getStock() {
        return stock;
    }

    public String getDescription() {
        return description;
    }
}
