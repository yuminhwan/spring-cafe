package com.prgrms.springcafe.product.dto;

import java.time.LocalDateTime;

import com.prgrms.springcafe.product.domain.Category;
import com.prgrms.springcafe.product.domain.Product;

public class ProductResponse {

    private final Long id;
    private final String name;
    private final Category category;
    private final long price;
    private final int stock;
    private final String description;
    private final LocalDateTime createdDateTime;
    private final LocalDateTime modifiedDateTime;

    public ProductResponse(Long id, String name, Category category, long price, int stock, String description,
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

    public static ProductResponse from(Product product) {
        return new ProductResponse(product.getId(), product.getName().getValue(), product.getCategory(),
            product.getPrice().getAmount(), product.getStock().getAmount(), product.getDescription(),
            product.getCreatedDateTime(), product.getModifiedDateTime());
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Category getCategory() {
        return category;
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

    public LocalDateTime getCreatedDateTime() {
        return createdDateTime;
    }

    public LocalDateTime getModifiedDateTime() {
        return modifiedDateTime;
    }
}
