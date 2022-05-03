package com.prgrms.springcafe.product.repository;

import static com.prgrms.springcafe.global.utils.JdbcUtils.*;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.sql.DataSource;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
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
    private final SimpleJdbcInsert simpleJdbcInsert;

    public ProductJdbcRepository(DataSource dataSource) {
        this.jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
        this.simpleJdbcInsert = new SimpleJdbcInsert(dataSource)
            .withTableName("product")
            .usingGeneratedKeyColumns("product_id");
    }

    @Override
    public Product insert(Product product) {
        Number newId = simpleJdbcInsert.executeAndReturnKey(toParamMap(product));
        return new Product(newId.longValue(), product.getName(), product.getCategory(), product.getPrice(),
            product.getStock(), product.getDescription(), product.getCreatedDateTime(), product.getModifiedDateTime());
    }

    @Override
    public void update(Product product) {
        int updateCnt = jdbcTemplate.update(
            "UPDATE product SET product_name = :product_name, category = :category, price = :price, stock = :stock, description = :description,"
                + " created_at = :created_at, updated_at = :updated_at WHERE product_id = :product_id",
            toParamMap(product));

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

    private Map<String, Object> toParamMap(Product product) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("product_id", product.getId());
        parameters.put("product_name", product.getName().getValue());
        parameters.put("category", product.getCategory().toString());
        parameters.put("price", product.getPrice().getAmount());
        parameters.put("stock", product.getStock().getAmount());
        parameters.put("description", product.getDescription());
        parameters.put("created_at", product.getCreatedDateTime());
        parameters.put("updated_at", product.getModifiedDateTime());
        return parameters;
    }
}
