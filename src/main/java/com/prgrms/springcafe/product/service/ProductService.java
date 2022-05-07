package com.prgrms.springcafe.product.service;

import java.util.List;
import java.util.Optional;

import com.prgrms.springcafe.product.domain.Category;
import com.prgrms.springcafe.product.domain.vo.ProductName;
import com.prgrms.springcafe.product.dto.CreateProductRequest;
import com.prgrms.springcafe.product.dto.ProductResponse;
import com.prgrms.springcafe.product.dto.UpdateProductRequest;

public interface ProductService {

    ProductResponse createProduct(CreateProductRequest productRequest);

    ProductResponse modifyProduct(Long id, UpdateProductRequest productRequest);

    List<ProductResponse> findProducts(Optional<Category> category);

    List<ProductResponse> findAllProducts();

    List<ProductResponse> findByCategory(Category category);

    ProductResponse findById(Long id);

    ProductResponse findByName(ProductName productName);

    void removeProduct(Long id);
}
