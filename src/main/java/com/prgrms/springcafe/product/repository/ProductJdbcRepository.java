package com.prgrms.springcafe.product.repository;

import static com.prgrms.springcafe.global.utils.JdbcUtils.*;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.prgrms.springcafe.product.domain.Category;
import com.prgrms.springcafe.product.domain.Product;
import com.prgrms.springcafe.product.domain.vo.Money;
import com.prgrms.springcafe.product.domain.vo.ProductName;
import com.prgrms.springcafe.product.domain.vo.Quantity;
import com.prgrms.springcafe.product.exception.ProductNotFoundException;

@Repository
public class ProductJdbcRepository implements ProductRepostiory {

    private static final int EXECUTE_VALUE = 1;
    private static final RowMapper<Product> productRowMapper = (resultSet, rowNum) -> {
        Long productId = resultSet.getLong("product_id");
        String productName = resultSet.getString("product_name");
        Category category = Category.valueOf(resultSet.getString("category"));
        long price = resultSet.getLong("price");
        int stock = resultSet.getInt("stock");
        String description = resultSet.getString("description");
        LocalDateTime createdAt = toLocalDateTime(resultSet.getTimestamp("created_at"));
        LocalDateTime updatedAt = toLocalDateTime(resultSet.getTimestamp("updated_at"));
        return new Product(productId, new ProductName(productName), category, new Money(price), new Quantity(stock),
            description, createdAt, updatedAt);
    };

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public ProductJdbcRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Product insert(Product product) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(
            "INSERT INTO product(product_name, category, price, stock, description, created_at, updated_at) " +
                "VALUES(:productName, :category, :price, :stock, :description, :createdAt, :updatedAt)",
            toParameterSource(product), keyHolder, new String[] {"product_id"});

        Number newId = keyHolder.getKey();
        return new Product(newId.longValue(), product.getName(), product.getCategory(), product.getPrice(),
            product.getStock(), product.getDescription(), product.getCreatedDateTime(), product.getModifiedDateTime());
    }

    @Override
    public void update(Product product) {
        int updateCnt = jdbcTemplate.update(
            "UPDATE product SET price = :price, stock = :stock, description = :description, updated_at = :updatedAt WHERE product_id = :productId",
            toParameterSource(product));

        if (updateCnt != EXECUTE_VALUE) {
            throw new ProductNotFoundException(product.getId());
        }
    }

    @Override
    public List<Product> findAll() {
        return jdbcTemplate.query("SELECT * FROM product", productRowMapper);
    }

    @Override
    public List<Product> findByCategory(Category category) {
        return jdbcTemplate.query("SELECT * FROM product WHERE category = :category",
            Collections.singletonMap("category", category.toString()),
            productRowMapper);
    }

    @Override
    public Optional<Product> findById(Long productId) {
        try {
            return Optional.ofNullable(
                jdbcTemplate.queryForObject("SELECT * FROM product WHERE product_id = :productId",
                    Collections.singletonMap("productId", productId),
                    productRowMapper));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<Product> findByName(ProductName productName) {
        try {
            return Optional.ofNullable(
                jdbcTemplate.queryForObject("SELECT * FROM product WHERE product_name = :productName",
                    Collections.singletonMap("productName", productName.getValue()),
                    productRowMapper));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public void deleteById(Long id) {
        int deletedCnt = jdbcTemplate.update("DELETE FROM product WHERE product_id = :productId",
            Collections.singletonMap("productId", id));

        if (deletedCnt != EXECUTE_VALUE) {
            throw new ProductNotFoundException(id);
        }
    }

    @Override
    public boolean existsByName(String name) {
        return Boolean.TRUE.equals(jdbcTemplate.queryForObject(
            "SELECT EXISTS (SELECT * FROM product WHERE product_name = :productName)",
            Collections.singletonMap("productName", name), Boolean.class));
    }

    private SqlParameterSource toParameterSource(Product product) {
        return new MapSqlParameterSource()
            .addValue("productId", product.getId())
            .addValue("productName", product.getName().getValue())
            .addValue("category", product.getCategory().toString())
            .addValue("price", product.getPrice().getAmount())
            .addValue("stock", product.getStock().getAmount())
            .addValue("description", product.getDescription())
            .addValue("createdAt", product.getCreatedDateTime())
            .addValue("updatedAt", product.getModifiedDateTime());
    }
}
