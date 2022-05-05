package com.prgrms.springcafe.product.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import org.hibernate.validator.constraints.Length;

import com.prgrms.springcafe.product.domain.Category;
import com.prgrms.springcafe.product.domain.Product;

public class CreateProductRequest {

    @NotBlank(message = "상품 이름에 공백만 있을 수 없습니다.")
    @Length(min = 3, max = 20, message = "상품 이름은 {min}글자 이상 {max}글자 이하여야합니다.")
    private String name;

    @NotNull(message = "카테고리는 필수로 필요합니다.")
    private Category category;

    @Positive(message = "상품 가격은 양수여야합니다.")
    private long price;
    
    @Positive(message = "상품 수량은 양수여야합니다.")
    private int stock;

    @Length(min = 5, max = 500, message = "상품 설명은 {min}글자 이상 {max}글자 이하여야합니다.")
    private String description;

    public CreateProductRequest() {

    }

    public CreateProductRequest(String name, Category category, long price, int stock, String description) {
        this.name = name;
        this.category = category;
        this.price = price;
        this.stock = stock;
        this.description = description;
    }

    public Product toEntity() {
        return new Product(name, category, price, stock, description);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
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
