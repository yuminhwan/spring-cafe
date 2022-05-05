package com.prgrms.springcafe.product.service;

import static java.util.stream.Collectors.*;

import java.text.MessageFormat;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.prgrms.springcafe.product.domain.Category;
import com.prgrms.springcafe.product.domain.Product;
import com.prgrms.springcafe.product.domain.vo.ProductName;
import com.prgrms.springcafe.product.dto.CreateProductRequest;
import com.prgrms.springcafe.product.dto.ProductResponse;
import com.prgrms.springcafe.product.dto.UpdateProductRequest;
import com.prgrms.springcafe.product.exception.NameDuplicateException;
import com.prgrms.springcafe.product.exception.ProductNotFoundException;
import com.prgrms.springcafe.product.repository.ProductRepostiory;

@Service
@Transactional
public class ProductService {

    private final ProductRepostiory productRepostiory;

    public ProductService(ProductRepostiory productRepostiory) {
        this.productRepostiory = productRepostiory;
    }

    public ProductResponse createProduct(CreateProductRequest productRequest) {
        validateDuplicateName(productRequest.getName());
        Product product = productRepostiory.insert(productRequest.toEntity());
        return ProductResponse.from(product);
    }

    private void validateDuplicateName(String name) {
        if (productRepostiory.existsByName(name)) {
            throw new NameDuplicateException(MessageFormat.format("입력한 상품명 {0}은 중복됩니다.", name));
        }
    }

    public ProductResponse modifyProduct(Long id, UpdateProductRequest productRequest) {
        Product product = productRepostiory.findById(id)
            .orElseThrow(() -> new ProductNotFoundException(id));

        product.changeInformation(productRequest.getPrice(), productRequest.getStock(), product.getDescription());
        productRepostiory.update(product);

        return ProductResponse.from(product);
    }

    @Transactional(readOnly = true)
    public List<ProductResponse> findAllProducts() {
        List<Product> products = productRepostiory.findAll();
        return products.stream()
            .map(ProductResponse::from)
            .collect(toList());
    }

    @Transactional(readOnly = true)
    public List<ProductResponse> findByCategory(Category category) {
        List<Product> products = productRepostiory.findByCategory(category);
        return products.stream()
            .map(ProductResponse::from)
            .collect(toList());
    }

    @Transactional(readOnly = true)
    public ProductResponse findById(Long id) {
        Product product = productRepostiory.findById(id)
            .orElseThrow(() -> new ProductNotFoundException(id));

        return ProductResponse.from(product);
    }

    @Transactional(readOnly = true)
    public ProductResponse findByName(ProductName productName) {
        Product product = productRepostiory.findByName(productName)
            .orElseThrow(() -> new ProductNotFoundException(productName.getValue()));

        return ProductResponse.from(product);
    }

    public void removeProduct(Long id) {
        productRepostiory.deleteById(id);
    }

}
