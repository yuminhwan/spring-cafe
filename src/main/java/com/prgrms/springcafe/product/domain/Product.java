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
    private Money price;
    private Quantity stock;
    private String description;
    private LocalDateTime modifiedDateTime;

    public Product(String name, Category category, long price, int stock, String description) {
        this(new ProductName(name), category, new Money(price), new Quantity(stock), description);
    }

    public Product(ProductName name, Category category, Money price, Quantity stock, String description) {
        this(null, name, category, price, stock, description, LocalDateTime.now(), LocalDateTime.now());
    }

    public Product(Long id, ProductName name, Category category, Money price, Quantity stock, String description,
        LocalDateTime createdDateTime, LocalDateTime modifiedDateTime) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.price = price;
        this.stock = stock;
        this.description = description;
        this.createdDateTime = createdDateTime;
        this.modifiedDateTime = modifiedDateTime;
    }

    public void sellProduct(int sellQuantity) {
        this.stock = stock.minusQuantity(sellQuantity);
    }

    public void changeInformation(String productName, Category category, long price, int stock,
        String description) {
        this.name = new ProductName(productName);
        this.category = category;
        this.price = new Money(price);
        this.stock = new Quantity(stock);
        this.description = description;
        this.modifiedDateTime = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getCreatedDateTime() {
        return createdDateTime;
    }

    public ProductName getName() {
        return name;
    }

    public Category getCategory() {
        return category;
    }

    public Money getPrice() {
        return price;
    }

    public Quantity getStock() {
        return stock;
    }

    public String getDescription() {
        return description;
    }

    public LocalDateTime getModifiedDateTime() {
        return modifiedDateTime;
    }
}
