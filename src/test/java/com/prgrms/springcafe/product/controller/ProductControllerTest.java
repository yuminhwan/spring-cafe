package com.prgrms.springcafe.product.controller;

import static com.prgrms.springcafe.product.domain.Category.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.prgrms.springcafe.product.dto.CreateProductRequest;
import com.prgrms.springcafe.product.dto.ProductResponse;
import com.prgrms.springcafe.product.dto.UpdateProductRequest;
import com.prgrms.springcafe.product.exception.NameDuplicateException;
import com.prgrms.springcafe.product.exception.ProductNotFoundException;
import com.prgrms.springcafe.product.service.ProductService;

@WebMvcTest(ProductController.class)
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    @DisplayName("전체 상품을 조회한다.")
    @Test
    void showProducts() throws Exception {
        // given
        List<ProductResponse> products = products();
        given(productService.findAllProducts()).willReturn(products);

        // when
        ResultActions resultActions = mockMvc.perform(get("/products"));

        // then
        resultActions.andExpect(status().isOk())
            .andExpect(model().attribute("products", products))
            .andExpect(view().name("product-list"));
    }

    @DisplayName("id로 상품을 조회한다.")
    @Test
    void showProductDetails() throws Exception {
        // given
        ProductResponse product = product();
        given(productService.findById(1L)).willReturn(product);

        // when
        ResultActions resultActions = mockMvc.perform(get("/products/{productId}", "1"));

        // then
        resultActions.andExpect(status().isOk())
            .andExpect(model().attribute("product", product))
            .andExpect(view().name("product-details"));
    }

    @DisplayName("저장되지 않은 id로 조회할 시 에러페이지로 이동한다.")
    @Test
    void showProductDetails_WithNotSavedId() throws Exception {
        // given
        Long id = 10L;
        given(productService.findById(id)).willThrow(new ProductNotFoundException(id));

        // when
        ResultActions resultActions = mockMvc.perform(get("/products/{productId}", "10"));

        // then
        resultActions.andExpect(status().is4xxClientError());
    }

    @DisplayName("상품 생성을 요청한다.")
    @Test
    void saveProduct() throws Exception {
        // given
        MultiValueMap<String, String> params = createProductRequest();

        // when
        ResultActions resultActions = mockMvc.perform(post("/products")
            .params(params));

        // then
        resultActions.andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/products"));
    }

    @DisplayName("상품 생성시 상품명이 중복될 시 에러페이지로 이동한다.")
    @Test
    void saveProduct_WithDuplicateName() throws Exception {
        // given
        MultiValueMap<String, String> params = createProductRequest();
        given(productService.createProduct(any(CreateProductRequest.class))).willThrow(
            new NameDuplicateException("입력한 상품명 cake은 중복됩니다."));

        // when
        ResultActions resultActions = mockMvc.perform(post("/products")
            .params(params));

        // then
        resultActions.andExpect(status().is4xxClientError());
    }

    @DisplayName("상품 수정을 요청한다.")
    @Test
    void modifyProduct() throws Exception {
        // given
        MultiValueMap<String, String> params = updateProductRequest();

        // when
        ResultActions resultActions = mockMvc.perform(post("/products/modify/{productId}", "1")
            .params(params));

        // then
        resultActions.andExpect(status().isCreated())
            .andExpect(redirectedUrl("/products"));
    }

    @DisplayName("저장되지 않은 id로 수정할 시 에러페이지로 이동한다.")
    @Test
    void modifyProduct_WithNotSavedId() throws Exception {
        // given
        Long id = 10L;
        MultiValueMap<String, String> params = updateProductRequest();
        given(productService.modifyProduct(eq(id), any(UpdateProductRequest.class))).willThrow(
            new ProductNotFoundException(id));

        // when
        ResultActions resultActions = mockMvc.perform(post("/products/modify/{productId}", "10")
            .params(params));

        // then
        resultActions.andExpect(status().is4xxClientError());
    }

    @DisplayName("상품 삭제를 요청한다.")
    @Test
    void deleteProduct() throws Exception {
        // given
        // when
        ResultActions resultActions = mockMvc.perform(post("/products/delete/{productId}", "1"));

        // then
        resultActions.andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/products"));
    }

    @DisplayName("저장되지 않은 id로 삭제할 시 에러페이지로 이동한다.")
    @Test
    void deleteProduct_WithNotSavedId() throws Exception {
        // given
        Long id = 10L;
        willThrow(new ProductNotFoundException(id)).given(productService).removeProduct(id);

        // when
        ResultActions resultActions = mockMvc.perform(post("/products/delete/{productId}", "10"));

        // then
        resultActions.andExpect(status().is4xxClientError());
    }

    private MultiValueMap<String, String> createProductRequest() {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("name", "cake");
        params.add("category", "CAKE");
        params.add("price", "1000");
        params.add("stock", "100");
        params.add("description", "cakeDelicious");
        return params;
    }

    private MultiValueMap<String, String> updateProductRequest() {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("price", "2000");
        params.add("stock", "200");
        params.add("description", "Delicious");
        return params;
    }

    private ProductResponse product() {
        return new ProductResponse(1L, "americano", COFFEE, 1000L, 100, "americano", LocalDateTime.now(),
            LocalDateTime.now());
    }

    private List<ProductResponse> products() {
        return List.of(
            new ProductResponse(1L, "americano", COFFEE, 1000L, 100, "americano", LocalDateTime.now(),
                LocalDateTime.now()),
            new ProductResponse(2L, "cheesecake", CAKE, 5000L, 50, "cheesecake", LocalDateTime.now(),
                LocalDateTime.now()));
    }

}
