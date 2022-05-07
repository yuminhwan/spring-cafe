package com.prgrms.springcafe.order.repository;

import java.util.Collections;
import java.util.Optional;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import com.prgrms.springcafe.order.domain.OrderItem;
import com.prgrms.springcafe.order.exception.OrderItemNotFoundException;
import com.prgrms.springcafe.vo.Money;
import com.prgrms.springcafe.vo.Quantity;

@Repository
public class OrderItemJdbcRepository implements OrderItemRepository {

    private static final int EXECUTE_VALUE = 1;
    private static final RowMapper<OrderItem> orderItemRowMapper = (resultSet, rowNum) -> {
        Long id = resultSet.getLong("id");
        Long orderId = resultSet.getLong("order_id");
        Long productId = resultSet.getLong("product_id");
        Money price = new Money(resultSet.getLong("price"));
        Quantity quantity = new Quantity(resultSet.getInt("quantity"));
        return new OrderItem(id, orderId, productId, price, quantity);

    };
    private final NamedParameterJdbcTemplate jdbcTemplate;

    public OrderItemJdbcRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void update(OrderItem orderItem) {
        int updateCnt = jdbcTemplate.update(
            "UPDATE order_item SET quantity = :quantity WHERE id = :orderItemId",
            toParameterSource(orderItem));

        if (updateCnt != EXECUTE_VALUE) {
            throw new OrderItemNotFoundException(orderItem.getOrderId());
        }
    }

    @Override
    public Optional<OrderItem> findById(Long id) {
        try {
            return Optional.ofNullable(
                jdbcTemplate.queryForObject("SELECT * FROM order_item WHERE id = :orderItemId",
                    Collections.singletonMap("orderItemId", id),
                    orderItemRowMapper));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public void deleteById(Long id) {
        int deletedCnt = jdbcTemplate.update("DELETE FROM order_item WHERE id = :orderItemId",
            Collections.singletonMap("orderItemId", id));

        if (deletedCnt != EXECUTE_VALUE) {
            throw new OrderItemNotFoundException(id);
        }
    }

    private SqlParameterSource toParameterSource(OrderItem orderItem) {
        return new MapSqlParameterSource()
            .addValue("orderItemId", orderItem.getId())
            .addValue("orderId", orderItem.getOrderId())
            .addValue("productId", orderItem.getProductId())
            .addValue("price", orderItem.getPrice().getAmount())
            .addValue("quantity", orderItem.getQuantity().getAmount());
    }
}
