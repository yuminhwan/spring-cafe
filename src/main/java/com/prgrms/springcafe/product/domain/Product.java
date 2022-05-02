package com.prgrms.springcafe.product.domain;

import java.time.LocalDateTime;

import com.prgrms.springcafe.product.domain.vo.Name;
import com.prgrms.springcafe.product.domain.vo.Price;
import com.prgrms.springcafe.product.domain.vo.Stock;

public class Product {

    private final Long id;
    private final LocalDateTime createdDateTime;
    private Name name;
    private Category category;
    private Price price;
    private Stock stock;
    private String description;
    private LocalDateTime modifiedDateTime;

    public Product(Long id, Name name, Category category, Price price, String description, Stock stock) {
        this(id, name, category, price, description, stock, LocalDateTime.now(), null);
    }

    public Product(Long id, Name name, Category category, Price price, String description, Stock stock,
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
        this.stock = this.stock.minusStock(sellQuantity);
    }

    public void changeInformation(Name name, Category category, Price price, Stock stock, String description) {
        this.name = name;
        this.category = category;
        this.price = price;
        this.stock = stock;
        this.description = description;
        this.modifiedDateTime = LocalDateTime.now();
    }
}
