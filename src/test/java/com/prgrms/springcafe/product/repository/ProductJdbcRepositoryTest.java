package com.prgrms.springcafe.product.repository;

import static org.assertj.core.api.Assertions.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.context.jdbc.Sql;

import com.prgrms.springcafe.product.domain.Category;
import com.prgrms.springcafe.product.domain.Product;
import com.prgrms.springcafe.product.exception.ProductNotFoundException;

@JdbcTest
@Sql({"classpath:productTest.sql"})
class ProductJdbcRepositoryTest {

    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;

    private ProductRepostiory productRepostiory;

    @BeforeEach
    void setup() {
        productRepostiory = new ProductJdbcRepository(jdbcTemplate);
    }

    @DisplayName("상품을 저장한다.")
    @Test
    void insert() {
        // given
        Product product = product();

        // when
        Product newProduct = productRepostiory.insert(product);

        // then
        assertThat(newProduct.getId()).isEqualTo(1L);
    }

    @DisplayName("상품을 수정한다.")
    @Test
    void update() {
        // given
        Product product = productRepostiory.insert(product());

        // when
        product.changeInformation(20L, 10, "so Delicious");
        productRepostiory.update(product);

        // then
        Optional<Product> findProduct = productRepostiory.findById(product.getId());
        assertThat(findProduct).isNotEmpty()
            .get()
            .usingRecursiveComparison()
            .isEqualTo(product);
    }

    @DisplayName("전체 상품을 조회한다.")
    @Test
    void findAll() {
        // given
        List<Product> products = products();

        // when
        products.forEach(productRepostiory::insert);

        // then
        assertThat(productRepostiory.findAll()).hasSize(2);
    }

    @DisplayName("특정 카테고리의 상품을 조회한다.")
    @Test
    void findByCategory() {
        // given
        products().forEach(productRepostiory::insert);

        // when
        List<Product> products = productRepostiory.findByCategory(Category.COFFEE);

        // then
        assertThat(products).hasSize(2);
    }

    @DisplayName("특정 id의 상품을 조회한다.")
    @Test
    void findById() {
        // given
        Product product = productRepostiory.insert(product());

        // when
        Optional<Product> findProduct = productRepostiory.findById(product.getId());

        // then
        assertThat(findProduct).isNotEmpty()
            .get()
            .usingRecursiveComparison()
            .isEqualTo(product);
    }

    @DisplayName("특정 이름의 상품을 조회한다.")
    @Test
    void findByName() {
        // given
        Product product = productRepostiory.insert(product());

        // when
        Optional<Product> findProduct = productRepostiory.findByName(product.getName());

        // then
        assertThat(findProduct).isNotEmpty()
            .get()
            .usingRecursiveComparison()
            .isEqualTo(product);
    }

    @DisplayName("상품을 제거한다.")
    @Test
    void deleteById() {
        // given
        Product product = productRepostiory.insert(product());

        // when
        // then
        assertThatCode(() -> productRepostiory.deleteById(product.getId()))
            .doesNotThrowAnyException();
    }

    @DisplayName("존재하지 않는 상품의 Id로 상품을 제거할 수 없다.")
    @Test
    void deleteById_WithNotSavedId() {
        assertThatThrownBy(() -> productRepostiory.deleteById(1L))
            .isInstanceOf(ProductNotFoundException.class)
            .hasMessage("Id가 1인 상품은 존재하지 않습니다.");
    }

    @DisplayName("특정 이름으로 저장된 상품이 있는 지 확인한다.")
    @ParameterizedTest
    @CsvSource(value = {"americano,true", "latte,false"})
    void existsByName(String name, boolean result) {
        // given
        productRepostiory.insert(product());

        // when
        boolean isExists = productRepostiory.existsByName(name);

        // then
        assertThat(isExists).isEqualTo(result);
    }

    private Product product() {
        return new Product("americano", Category.COFFEE, 1000L, 100, "americano");
    }

    private List<Product> products() {
        return List.of(
            new Product("americano", Category.COFFEE, 1000L, 100, "americano"),
            new Product("Latte", Category.COFFEE, 2000L, 100, "Latte")
        );
    }
}
