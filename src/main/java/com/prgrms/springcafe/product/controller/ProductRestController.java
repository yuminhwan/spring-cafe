package com.prgrms.springcafe.product.controller;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.prgrms.springcafe.product.domain.Category;
import com.prgrms.springcafe.product.dto.CreateProductRequest;
import com.prgrms.springcafe.product.dto.ProductResponse;
import com.prgrms.springcafe.product.dto.UpdateProductRequest;
import com.prgrms.springcafe.product.service.ProductService;

@RestController
@RequestMapping("/api/v1")
public class ProductRestController {

    private final ProductService productService;

    public ProductRestController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/products")
    public ResponseEntity<List<ProductResponse>> getProducts(@RequestParam Optional<Category> category) {
        return ResponseEntity.ok(productService.findProducts(category));
    }

    @GetMapping("/products/{id}")
    public ResponseEntity<ProductResponse> getProduct(@PathVariable Long id) {
        return ResponseEntity.ok(productService.findById(id));
    }

    @PostMapping("/products")
    public ResponseEntity<ProductResponse> createProduct(@Valid @RequestBody CreateProductRequest productRequest) {
        ProductResponse product = productService.createProduct(productRequest);
        return ResponseEntity.created(URI.create("/products/" + product.getId())).body(product);
    }

    @PutMapping("/products/{id}")
    public ResponseEntity<ProductResponse> modifyProduct(@PathVariable Long id,
        @Valid @RequestBody UpdateProductRequest productRequest) {
        return ResponseEntity.ok(productService.modifyProduct(id, productRequest));
    }

    @DeleteMapping("/products/{id}")
    public ResponseEntity<Void> removeProduct(@PathVariable Long id) {
        productService.removeProduct(id);
        return ResponseEntity.noContent().build();
    }
}
