package com.prgrms.springcafe.order.domain;

import com.prgrms.springcafe.vo.Money;
import com.prgrms.springcafe.vo.Quantity;

public class OrderItem {

    private final Long id;
    private final Long orderId;
    private final Long productId;
    private final Money price;
    private Quantity quantity;
    private Money totalPrice;

    public OrderItem(Long productId, long price, int quantity) {
        this(null, null, productId, new Money(price), new Quantity(quantity));
    }

    public OrderItem(Long productId, Money price, Quantity quantity) {
        this(null, null, productId, price, quantity);
    }

    public OrderItem(Long id, Long orderId, Long productId, Money price, Quantity quantity) {
        this.id = id;
        this.orderId = orderId;
        this.productId = productId;
        this.price = price;
        this.quantity = quantity;
        this.totalPrice = this.price.calculateTotal(quantity);
    }

    public void changeQuantity(Quantity quantity) {
        this.quantity = quantity;
        this.totalPrice = price.calculateTotal(quantity);
    }

    public boolean isLowerQuantity(Quantity quantity) {
        return this.quantity.isUnder(quantity);
    }

    public boolean isSameId(Long id) {
        return this.id.equals(id);
    }

    public Long getId() {
        return id;
    }

    public Long getOrderId() {
        return orderId;
    }

    public Long getProductId() {
        return productId;
    }

    public Money getPrice() {
        return price;
    }

    public Quantity getQuantity() {
        return quantity;
    }

    public Money getTotalPrice() {
        return totalPrice;
    }
}
