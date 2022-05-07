package com.prgrms.springcafe.product.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.prgrms.springcafe.product.domain.Category;
import com.prgrms.springcafe.product.dto.CreateProductRequest;
import com.prgrms.springcafe.product.dto.ProductResponse;
import com.prgrms.springcafe.product.dto.UpdateProductRequest;
import com.prgrms.springcafe.product.service.ProductService;

@Controller
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/products")
    public String showProductsPage(Model model) {
        List<ProductResponse> products = productService.findAllProducts();
        model.addAttribute("products", products);
        return "product/product-list";
    }

    @GetMapping("/new-product")
    public String newProductPage(Model model) {
        model.addAttribute("categorys", Category.toStringAll());
        model.addAttribute("product", new CreateProductRequest());
        return "product/new-product";
    }

    @PostMapping("/products")
    public String createProduct(@ModelAttribute("product") @Valid CreateProductRequest productRequest,
        BindingResult bindingResult, Model model) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("categorys", Category.toStringAll());
            return "product/new-product";
        }

        productService.createProduct(productRequest);
        return "redirect:/products";
    }

    @GetMapping("/products/{productId}")
    public String showProductDetails(@PathVariable("productId") Long productId, Model model) {
        ProductResponse productResponse = productService.findById(productId);
        model.addAttribute("product", productResponse);
        return "product/product-details";
    }

    @PostMapping("/products/{productId}")
    public String showModifyPage(@ModelAttribute("product") UpdateProductRequest productRequest,
        @ModelAttribute("productId") @PathVariable Long productId) {

        return "product/modify-product";
    }

    @PostMapping("/products/modify/{productId}")
    public String modifyProduct(@PathVariable Long productId,
        @ModelAttribute("product") @Valid UpdateProductRequest productRequest, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return "product/modify-product";
        }

        productService.modifyProduct(productId, productRequest);
        return "redirect:/products";
    }

    @PostMapping("/products/delete/{productId}")
    public String deleteProduct(@PathVariable("productId") Long productId) {
        productService.removeProduct(productId);
        return "redirect:/products";
    }
}
