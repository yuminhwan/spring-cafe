package com.prgrms.springcafe.product.repository;

import static org.assertj.core.api.Assertions.*;

import java.util.List;
import java.util.Optional;

import javax.sql.DataSource;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;

import com.prgrms.springcafe.product.domain.Category;
import com.prgrms.springcafe.product.domain.Product;
import com.prgrms.springcafe.product.domain.vo.Money;
import com.prgrms.springcafe.product.domain.vo.ProductName;
import com.prgrms.springcafe.product.domain.vo.Quantity;

@JdbcTest
class ProductJdbcRepositoryTest {

    @Autowired
    private DataSource dataSource;

    private ProductRepostiory productRepostiory;

    @BeforeEach
    void setup() {
        productRepostiory = new ProductJdbcRepository(dataSource);
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
        assertThat(newProduct.getName()).isEqualTo(new ProductName("americano"));
    }

    @DisplayName("상품을 수정한다.")
    @Test
    void update() {
        // given
        Product product = productRepostiory.insert(product());

        // when
        product.changeInformation(new ProductName("Latte"), Category.COFFEE, new Money(20), new Quantity(10), "Latte");
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
