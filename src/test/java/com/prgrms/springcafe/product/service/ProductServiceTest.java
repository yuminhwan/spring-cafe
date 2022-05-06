package com.prgrms.springcafe.product.service;

import static com.prgrms.springcafe.product.domain.Category.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.prgrms.springcafe.product.domain.Product;
import com.prgrms.springcafe.product.domain.vo.ProductName;
import com.prgrms.springcafe.product.dto.CreateProductRequest;
import com.prgrms.springcafe.product.dto.ProductResponse;
import com.prgrms.springcafe.product.dto.UpdateProductRequest;
import com.prgrms.springcafe.product.exception.NameDuplicateException;
import com.prgrms.springcafe.product.exception.ProductNotFoundException;
import com.prgrms.springcafe.product.repository.ProductRepostiory;
import com.prgrms.springcafe.vo.Money;
import com.prgrms.springcafe.vo.Quantity;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepostiory productRepostiory;

    @InjectMocks
    private ProductService productService;

    @DisplayName("상품을 생성한다.")
    @Test
    void createProduct() {
        // given
        CreateProductRequest productRequest = productRequest();
        Product product = product();
        given(productRepostiory.existsByName(any(String.class))).willReturn(false);
        given(productRepostiory.insert(any(Product.class))).willReturn(product);

        // when
        ProductResponse productResponse = productService.createProduct(productRequest);

        // then
        assertThat(productResponse.getId()).isEqualTo(1L);
        then(productRepostiory).should(times(1)).existsByName(any(String.class));
        then(productRepostiory).should(times(1)).insert(any(Product.class));
    }

    @DisplayName("중복된 이름의 상품은 생성할 수 없다.")
    @Test
    void createProduct_WithDuplicateName() {
        // given
        CreateProductRequest productRequest = productRequest();
        given(productRepostiory.existsByName(any(String.class))).willReturn(true);

        // when
        // then
        assertThatThrownBy(() -> productService.createProduct(productRequest))
            .isInstanceOf(NameDuplicateException.class)
            .hasMessageContaining("중복됩니다.");
    }

    @DisplayName("상품을 수정한다.")
    @Test
    void modifyProduct() {
        // given
        Product product = product();
        given(productRepostiory.findById(product.getId())).willReturn(Optional.of(product));
        UpdateProductRequest productRequest = updateProductRequest();

        // when
        ProductResponse productResponse = productService.modifyProduct(1L, productRequest);

        // then
        assertThat(productResponse.getId()).isEqualTo(1L);
        assertThat(productResponse.getPrice()).isEqualTo(3000L);
        then(productRepostiory).should(times(1)).findById(1L);
        then(productRepostiory).should(times(1)).update(any(Product.class));
    }

    @DisplayName("저장되지 않은 Id로 상품을 수정할 수 없다.")
    @Test
    void modifyProduct_WithNotSavedId() {
        // given
        Long id = 2L;
        UpdateProductRequest updateProductRequest = updateProductRequest();
        given(productRepostiory.findById(id)).willReturn(Optional.empty());

        // when
        // then
        assertThatThrownBy(() -> productService.modifyProduct(id, updateProductRequest))
            .isInstanceOf(ProductNotFoundException.class)
            .hasMessage("Id가 2인 상품은 존재하지 않습니다.");
    }

    @DisplayName("전체 상품을 조회한다.")
    @Test
    void findAllProducts() {
        // given
        List<Product> products = List.of(product());
        given(productRepostiory.findAll()).willReturn(products);

        // when
        List<ProductResponse> productResponses = productService.findAllProducts();

        // then
        assertThat(productResponses).hasSize(1);
        then(productRepostiory).should(times(1)).findAll();
    }

    @DisplayName("특정 카테고리의 상품을 조회한다.")
    @Test
    void findByCategory() {
        // given
        List<Product> products = List.of(product());
        given(productRepostiory.findByCategory(COFFEE)).willReturn(products);

        // when
        List<ProductResponse> findProducts = productService.findByCategory(COFFEE);

        // then
        assertThat(findProducts).extracting("category").containsExactly(COFFEE);
        then(productRepostiory).should(times(1)).findByCategory(COFFEE);
    }

    @DisplayName("특정 id의 상품을 조회한다.")
    @Test
    void findById() {
        // given
        Product product = product();
        given(productRepostiory.findById(1L)).willReturn(Optional.of(product));

        // when
        ProductResponse productResponse = productService.findById(1L);

        // then
        assertThat(productResponse.getId()).isEqualTo(1L);
        then(productRepostiory).should(times(1)).findById(1L);
    }

    @DisplayName("저장되지 않은 Id로 조회할 수 없다.")
    @Test
    void findById_WithNotSavedId() {
        // given
        Long id = 2L;
        given(productRepostiory.findById(id)).willReturn(Optional.empty());

        // when
        // then
        assertThatThrownBy(() -> productService.findById(id))
            .isInstanceOf(ProductNotFoundException.class)
            .hasMessage("Id가 2인 상품은 존재하지 않습니다.");
        then(productRepostiory).should(times(1)).findById(id);
    }

    @DisplayName("특정 이름의 상품을 조회한다.")
    @Test
    void findByName() {
        // given
        Product product = product();
        ProductName productName = product.getName();
        given(productRepostiory.findByName(productName)).willReturn(Optional.of(product));

        // when
        ProductResponse productResponse = productService.findByName(productName);

        // then
        assertThat(productResponse.getName()).isEqualTo("americano");
        then(productRepostiory).should(times(1)).findByName(productName);
    }

    @DisplayName("저장되지 않은 이름으로 조회할 수 없다.")
    @Test
    void findByName_WithNotSavedName() {
        // given
        ProductName productName = new ProductName("latte");
        given(productRepostiory.findByName(productName)).willReturn(Optional.empty());

        // when
        // then
        assertThatThrownBy(() -> productService.findByName(productName))
            .isInstanceOf(ProductNotFoundException.class)
            .hasMessage("상품명이 latte인 상품은 존재하지 않습니다.");
        then(productRepostiory).should(times(1)).findByName(productName);
    }

    @DisplayName("상품을 삭제한다.")
    @Test
    void removeProduct() {
        // given
        Long id = 1L;

        // when
        productService.removeProduct(id);

        // then
        then(productRepostiory).should(times(1)).deleteById(id);
    }

    private Product product() {
        return new Product(1L, new ProductName("americano"), COFFEE, new Money(2500L), new Quantity(100), "americano",
            LocalDateTime.now(), LocalDateTime.now());
    }

    private CreateProductRequest productRequest() {
        return new CreateProductRequest("americano", COFFEE, 2500L, 100, "americano");
    }

    private UpdateProductRequest updateProductRequest() {
        return new UpdateProductRequest(3000L, 50, "latte");
    }

}
