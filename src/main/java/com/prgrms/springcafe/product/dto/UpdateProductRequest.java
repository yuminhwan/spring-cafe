package com.prgrms.springcafe.product.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

import org.hibernate.validator.constraints.Length;

public class UpdateProductRequest {

    @NotNull(message = "상품 가격은 반드시 값이 필요합니다.")
    @Positive(message = "상품 가격은 양수여야합니다.")
    private Long price;

    @NotNull(message = "상품 수량은 반드시 값이 필요합니다.")
    @PositiveOrZero(message = "상품 수량은 0 또는 양수여야합니다.")
    private Integer stock;

    @Length(min = 5, max = 100, message = "상품 설명은 {min}글자 이상 {max}글자 이하여야합니다.")
    private String description;

    public UpdateProductRequest() {
    }

    public UpdateProductRequest(long price, int stock, String description) {
        this.price = price;
        this.stock = stock;
        this.description = description;
    }

    public long getPrice() {
        return price;
    }

    public void setPrice(long price) {
        this.price = price;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
