package com.prgrms.springcafe.product.repository;

import java.util.List;
import java.util.Optional;

import com.prgrms.springcafe.product.domain.Product;
import com.prgrms.springcafe.product.domain.vo.ProductName;

public interface ProductRepostiory {

    Product insert(Product product);

    void update(Product product);

    List<Product> findAll();

    Optional<Product> findById(Long productId);

    Optional<Product> findByName(ProductName productName);

    void deleteById(Long id);

    boolean existsByName(String name);
}
