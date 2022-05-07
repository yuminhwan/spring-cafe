package com.prgrms.springcafe.order.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import com.prgrms.springcafe.order.domain.OrderItem;

public class CreateOrderItemRequest {

    @NotNull(message = "상품 ID는 반드시 값이 필요합니다.")
    @Positive(message = "상품 ID는 양수여야 합니다.")
    private Long productId;

    @NotNull(message = "가격은 반드시 값이 필요합니다.")
    @Positive(message = "상품 가격은 양수여야 합니다.")
    private Long price;

    @NotNull(message = "상품 수량은 반드시 값이 필요합니다.")
    @Positive(message = "상품 수량은 양수여야 합니다.")
    private Integer quantity;

    public CreateOrderItemRequest() {
    }

    public CreateOrderItemRequest(Long productId, Long price, Integer quantity) {
        this.productId = productId;
        this.price = price;
        this.quantity = quantity;
    }

    public OrderItem toEntity() {
        return new OrderItem(productId, price, quantity);
    }

    public Long getProductId() {
        return productId;
    }

    public Long getPrice() {
        return price;
    }

    public Integer getQuantity() {
        return quantity;
    }
}
