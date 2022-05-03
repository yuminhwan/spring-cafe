package com.prgrms.springcafe.product.domain;

import java.time.LocalDateTime;

import com.prgrms.springcafe.product.domain.vo.Money;
import com.prgrms.springcafe.product.domain.vo.ProductName;
import com.prgrms.springcafe.product.domain.vo.Quantity;

public class Product {

    private final Long id;
    private final LocalDateTime createdDateTime;
    private ProductName name;
    private Category category;
    private Money money;
    private Quantity quantity;
    private String description;
    private LocalDateTime modifiedDateTime;

    public Product(Long id, ProductName name, Category category, Money money, String description, Quantity quantity) {
        this(id, name, category, money, description, quantity, LocalDateTime.now(), null);
    }

    public Product(Long id, ProductName name, Category category, Money money, String description,
        Quantity quantity,
        LocalDateTime createdDateTime, LocalDateTime modifiedDateTime) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.money = money;
        this.quantity = quantity;
        this.description = description;
        this.createdDateTime = createdDateTime;
        this.modifiedDateTime = modifiedDateTime;
    }

    public void sellProduct(int sellQuantity) {
        this.quantity = quantity.minusQuantity(sellQuantity);
    }

    public void changeInformation(ProductName productName, Category category, Money money, Quantity quantity,
        String description) {
        this.name = productName;
        this.category = category;
        this.money = money;
        this.quantity = quantity;
        this.description = description;
        this.modifiedDateTime = LocalDateTime.now();
    }
}
