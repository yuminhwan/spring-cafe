package com.prgrms.springcafe.product.controller;

import static com.prgrms.springcafe.product.domain.Category.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.prgrms.springcafe.product.dto.CreateProductRequest;
import com.prgrms.springcafe.product.dto.ProductResponse;
import com.prgrms.springcafe.product.dto.UpdateProductRequest;
import com.prgrms.springcafe.product.exception.NameDuplicateException;
import com.prgrms.springcafe.product.exception.ProductNotFoundException;
import com.prgrms.springcafe.product.service.ProductService;

@WebMvcTest(ProductRestController.class)
class ProductRestControllerTest {

    @MockBean
    private ProductService productService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @DisplayName("전체 상품을 조회한다.")
    @Test
    void getProducts() throws Exception {
        // given
        List<ProductResponse> products = products();
        given(productService.findProducts(Optional.empty())).willReturn(products);

        // when
        ResultActions resultActions = mockMvc.perform(get("/api/v1/products"));

        // then
        MvcResult mvcResult = resultActions.andExpect(status().isOk()).andReturn();
        ProductResponse[] productResponses = objectMapper.readValue(mvcResult.getResponse().getContentAsString(),
            ProductResponse[].class);
        assertThat(productResponses).hasSize(2);
    }

    @DisplayName("카테고리로 상품들을 조회한다.")
    @Test
    void getProducts_WithCategory() throws Exception {
        // given
        List<ProductResponse> products = List.of(product());
        given(productService.findProducts(Optional.of(COFFEE))).willReturn(products);

        // when
        ResultActions resultActions = mockMvc.perform(get("/api/v1/products")
            .param("category", "COFFEE"));

        // then
        MvcResult mvcResult = resultActions.andExpect(status().isOk()).andReturn();
        ProductResponse[] productResponses = objectMapper.readValue(mvcResult.getResponse().getContentAsString(),
            ProductResponse[].class);
        assertThat(productResponses).hasSize(1);
    }

    @DisplayName("입력한 카테고리가 잘못된 값이라면 에러 메시지를 반환한다.")
    @Test
    void getProducts_WithWrongCategory() throws Exception {
        // given

        // when
        ResultActions resultActions = mockMvc.perform(get("/api/v1/products")
            .param("category", "COFFE"));

        // then
        resultActions.andExpect(status().isBadRequest())
            .andExpect(jsonPath("errorMessage", "잘못된 입력입니다.").exists());
    }

    @DisplayName("Id로 상품을 조회한다.")
    @Test
    void getProduct() throws Exception {
        // given
        ProductResponse product = product();
        given(productService.findById(1L)).willReturn(product);

        // when
        ResultActions resultActions = mockMvc.perform(get("/api/v1/products/{id}", 1L));

        // then
        resultActions.andExpect(status().isOk());
        checkResponse(product, resultActions);
    }

    @DisplayName("저장되지 않은 id로 조회할 시 에러 메시지를 반환한다.")
    @Test
    void getProduct_WithNoSavedId() throws Exception {
        // given
        Long id = 10L;
        given(productService.findById(id)).willThrow(new ProductNotFoundException(id));

        // when
        ResultActions resultActions = mockMvc.perform(get("/api/v1/products/{id}", 10L));

        // then
        resultActions.andExpect(status().isNotFound())
            .andExpect(jsonPath("errorMessage", "Id가 10인 상품은 존재하지 않습니다.").exists());
    }

    @DisplayName("상품 생성을 요청한다.")
    @Test
    void createProduct() throws Exception {
        // given
        ProductResponse product = product();
        CreateProductRequest productRequest = createProductRequest();
        given(productService.createProduct(any(CreateProductRequest.class))).willReturn(product);

        // when
        ResultActions resultActions = mockMvc.perform(post("/api/v1/products")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(productRequest)));

        // then
        resultActions.andExpect(status().isCreated());
        checkResponse(product, resultActions);
    }

    @DisplayName("중복되는 이름으로 생성 요청 시 에러 메시지를 반환한다.")
    @Test
    void createProduct_WithDuplicateName() throws Exception {
        // given
        CreateProductRequest productRequest = createProductRequest();
        given(productService.createProduct(any(CreateProductRequest.class))).willThrow(
            new NameDuplicateException(productRequest.getName()));

        // when
        ResultActions resultActions = mockMvc.perform(post("/api/v1/products")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(productRequest)));

        // then
        resultActions.andExpect(status().isBadRequest())
            .andExpect(jsonPath("errorMessage", "입력한 상품명 americano은 중복됩니다.").exists());
    }

    @DisplayName("생성 요청값이 잘못되었을 경우 에러 메시지를 반환한다.")
    @Test
    void createProduct_UnValidRequest() throws Exception {
        // given
        CreateProductRequest productRequest = new CreateProductRequest("cake", CAKE, 0, 100, "cakeDelicious");

        // when
        ResultActions resultActions = mockMvc.perform(post("/api/v1/products")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(productRequest)));

        // then
        resultActions.andExpect(status().isBadRequest())
            .andExpect(jsonPath("errorMessage", "상품 가격은 양수여야합니다.").exists());
    }

    @DisplayName("상품 수정을 요청한다.")
    @Test
    void modifyProduct() throws Exception {
        // given
        ProductResponse product = new ProductResponse(1L, "americano", COFFEE, 3000L, 50, "latte", LocalDateTime.now(),
            LocalDateTime.now());
        UpdateProductRequest productRequest = updateProductRequest();
        given(productService.modifyProduct(eq(1L), any(UpdateProductRequest.class))).willReturn(product);

        // when
        ResultActions resultActions = mockMvc.perform(put("/api/v1/products/{id}", 1L)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(productRequest)));

        // then
        resultActions.andExpect(status().isOk());
        checkResponse(product, resultActions);
    }

    @DisplayName("저장되지 않은 id로 수정할 시 에러 메시지를 반환한다.")
    @Test
    void modifyProduct_WithNotSavedId() throws Exception {
        // given
        Long id = 10L;
        UpdateProductRequest productRequest = updateProductRequest();
        given(productService.modifyProduct(eq(id), any(UpdateProductRequest.class))).willThrow(
            new ProductNotFoundException(id));

        // when
        ResultActions resultActions = mockMvc.perform(put("/api/v1/products/{id}", id)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(productRequest)));

        // then
        resultActions.andExpect(status().isNotFound())
            .andExpect(jsonPath("errorMessage", "Id가 10인 상품은 존재하지 않습니다.").exists());
    }

    @DisplayName("수정 요청값이 잘못되었을 경우 에러 메시지를 반환한다.")
    @Test
    void modifyProduct_UnValidRequest() throws Exception {
        // given
        UpdateProductRequest productRequest = new UpdateProductRequest(3000L, -1, "latte");

        // when
        ResultActions resultActions = mockMvc.perform(put("/api/v1/products/{id}", 1L)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(productRequest)));

        // then
        resultActions.andExpect(status().isBadRequest())
            .andExpect(jsonPath("errorMessage", "상품 수량은 양수여야합니다.").exists());
    }

    @DisplayName("상품 삭제를 요청한다.")
    @Test
    void removeProduct() throws Exception {
        // given
        Long id = 1L;

        // when
        ResultActions resultActions = mockMvc.perform(delete("/api/v1/products/{id}", id));

        // then
        resultActions.andExpect(status().isNoContent());
    }

    @DisplayName("저장되지 않은 id로 삭제할 시 에러 메시지를 반환한다.")
    @Test
    void removeProduct_WithNotSavedId() throws Exception {
        // given
        Long id = 10L;
        willThrow(new ProductNotFoundException(id)).given(productService).removeProduct(id);

        // when
        ResultActions resultActions = mockMvc.perform(delete("/api/v1/products/{id}", id));

        // then
        resultActions.andExpect(status().isNotFound())
            .andExpect(jsonPath("errorMessage", "Id가 10인 상품은 존재하지 않습니다.").exists());
    }

    private void checkResponse(ProductResponse product, ResultActions resultActions) throws Exception {
        resultActions.andExpect(jsonPath("id", product.getId()).exists())
            .andExpect(jsonPath("name", product.getName()).exists())
            .andExpect(jsonPath("category", product.getCategory()).exists())
            .andExpect(jsonPath("price", product.getPrice()).exists())
            .andExpect(jsonPath("stock", product.getStock()).exists())
            .andExpect(jsonPath("description", product.getDescription()).exists())
            .andExpect(jsonPath("createdDateTime", product.getCreatedDateTime()).exists())
            .andExpect(jsonPath("modifiedDateTime", product.getModifiedDateTime()).exists());
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

    private CreateProductRequest createProductRequest() {
        return new CreateProductRequest("americano", COFFEE, 2500L, 100, "americano");
    }

    private UpdateProductRequest updateProductRequest() {
        return new UpdateProductRequest(3000L, 50, "latte");
    }

}
