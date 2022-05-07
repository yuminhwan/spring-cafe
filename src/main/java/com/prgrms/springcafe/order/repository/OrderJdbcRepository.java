package com.prgrms.springcafe.order.repository;

import static com.prgrms.springcafe.global.utils.JdbcUtils.*;
import static java.util.stream.Collectors.*;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.prgrms.springcafe.order.domain.Order;
import com.prgrms.springcafe.order.domain.OrderItem;
import com.prgrms.springcafe.order.domain.OrderItems;
import com.prgrms.springcafe.order.domain.OrderStatus;
import com.prgrms.springcafe.order.domain.Orderer;
import com.prgrms.springcafe.order.domain.vo.Address;
import com.prgrms.springcafe.order.domain.vo.Email;
import com.prgrms.springcafe.order.exception.OrderItemNotFoundException;
import com.prgrms.springcafe.vo.Money;
import com.prgrms.springcafe.vo.Quantity;

@Repository
public class OrderJdbcRepository implements OrderRepository {

    private static final int EXECUTE_VALUE = 1;

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public OrderJdbcRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Order insert(Order order) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(
            "INSERT INTO `order`(email, address, postcode, order_status, created_at, updated_at)"
                + "VALUES(:email, :address, :postcode, :orderStatus, :createdAt, :updatedAt)",
            toOrderParameterSource(order), keyHolder, new String[] {"order_id"});
        Long orderId = keyHolder.getKey().longValue();

        List<OrderItem> orderItems = order.getOrderItems().getOrderItems().stream()
            .map(orderItem -> insertOrderItem(orderId, order, orderItem))
            .collect(toList());

        return new Order(orderId, order.getOrderer(), new OrderItems(orderItems), order.getOrderStatus(),
            order.getCreatedDateTime(), order.getModifiedDateTime());
    }

    private OrderItem insertOrderItem(Long orderId, Order order, OrderItem orderItem) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(
            "INSERT INTO order_item(order_id, product_id, price, quantity, created_at, updated_at) "
                + "VALUES(:orderId, :productId, :price, :quantity, :createdAt, :updatedAt)",
            toOrderItemParameterSource(orderId, order.getCreatedDateTime(), order.getModifiedDateTime(), orderItem),
            keyHolder, new String[] {"id"}
        );
        Long orderItemId = keyHolder.getKey().longValue();
        return new OrderItem(orderItemId, orderId, orderItem.getProductId(), orderItem.getPrice(),
            orderItem.getQuantity());
    }

    @Override
    public void updateAddress(Order order) {
        int updateCnt = jdbcTemplate.update(
            "UPDATE `order` SET address = :address, postcode = :postcode WHERE order_id = :orderId",
            toOrderParameterSource(order));

        if (updateCnt != EXECUTE_VALUE) {
            throw new OrderItemNotFoundException(order.getId());
        }
    }

    @Override
    public List<Order> findAll() {
        List<Map<String, Object>> result = jdbcTemplate.queryForList(
            "SELECT O.order_id AS order_id, O.email AS email, O.address AS address, O.postcode AS postcode, O.order_status AS order_status,"
                + " O.created_at AS created_at, O.updated_at AS updated_at,I.id AS item_id, I.product_id AS product_id, I.price AS price, I.quantity AS quantity"
                + " FROM `order` AS O "
                + "JOIN order_item I on O.order_id = I.order_id",
            Collections.emptyMap());

        return mapOrders(result);
    }

    @Override
    public List<Order> findByEmail(Email email) {
        List<Map<String, Object>> result = jdbcTemplate.queryForList(
            "SELECT O.order_id AS order_id, O.email AS email, O.address AS address, O.postcode AS postcode, O.order_status AS order_status,"
                + " O.created_at AS created_at, O.updated_at AS updated_at, I.id AS item_id, I.product_id AS product_id, I.price AS price, I.quantity AS quantity"
                + " FROM `order` AS O "
                + "JOIN order_item I on O.order_id = I.order_id"
                + " WHERE O.email = :email",
            Collections.singletonMap("email", email.getEmail()));

        if (result.size() == 0) {
            return Collections.emptyList();
        }

        return mapOrders(result);
    }

    private List<Order> mapOrders(List<Map<String, Object>> result) {
        Map<Long, List<Map<String, Object>>> resultByOrder = result.stream()
            .collect(groupingBy(it -> (Long)it.get("order_id")));

        return resultByOrder.values().stream()
            .map(this::mapOrder)
            .collect(toList());
    }

    @Override
    public Optional<Order> findById(Long id) {
        List<Map<String, Object>> result = jdbcTemplate.queryForList(
            "SELECT O.order_id AS order_id, O.email AS email, O.address AS address, O.postcode AS postcode, O.order_status AS order_status,"
                + " O.created_at AS created_at, O.updated_at AS updated_at, I.id AS item_id, I.product_id AS product_id, I.price AS price, I.quantity AS quantity"
                + " FROM `order` AS O "
                + "JOIN order_item I on O.order_id = I.order_id"
                + " WHERE I.order_id = :orderId",
            Collections.singletonMap("orderId", id));

        if (result.size() == 0) {
            return Optional.empty();
        }

        return Optional.of(mapOrder(result));
    }

    private Order mapOrder(List<Map<String, Object>> result) {
        Long orderId = (Long)result.get(0).get("order_id");
        Email email = new Email((String)result.get(0).get("email"));
        Address address = new Address((String)result.get(0).get("address"), (String)result.get(0).get("postcode"));
        OrderStatus orderStatus = OrderStatus.valueOf((String)result.get(0).get("order_status"));
        LocalDateTime createdDateTime = toLocalDateTime((Timestamp)result.get(0).get("created_at"));
        LocalDateTime modifiedDateTime = toLocalDateTime((Timestamp)result.get(0).get(("updated_at")));
        OrderItems orderItems = new OrderItems(extractOrderItems(result));

        return new Order(orderId, new Orderer(email, address), orderItems, orderStatus, createdDateTime,
            modifiedDateTime);
    }

    private List<OrderItem> extractOrderItems(List<Map<String, Object>> result) {
        if (Objects.isNull(result.get(0).get("item_id"))) {
            return Collections.emptyList();
        }

        return result.stream()
            .map(this::mapOrderItem)
            .collect(toList());
    }

    private OrderItem mapOrderItem(Map<String, Object> result) {
        return new OrderItem(
            (Long)result.get("item_id"),
            (Long)result.get("order_id"),
            (Long)result.get("product_id"),
            new Money((Long)result.get("price")),
            new Quantity((int)result.get("quantity")));
    }

    @Override
    public void deleteById(Long id) {
        int deletedCnt = jdbcTemplate.update("DELETE FROM `order` WHERE order_id = :orderItemId",
            Collections.singletonMap("orderItemId", id));

        if (deletedCnt != EXECUTE_VALUE) {
            throw new OrderItemNotFoundException(id);
        }
    }

    @Override
    public void updateStatus(Order order) {
        int updateCnt = jdbcTemplate.update(
            "UPDATE `order` SET order_status = :orderStatus WHERE order_id = :orderId",
            toOrderParameterSource(order));

        if (updateCnt != EXECUTE_VALUE) {
            throw new OrderItemNotFoundException(order.getId());
        }
    }

    private SqlParameterSource toOrderParameterSource(Order order) {
        return new MapSqlParameterSource()
            .addValue("orderId", order.getId())
            .addValue("email", order.getOrderer().getEmail().getEmail())
            .addValue("address", order.getOrderer().getAddress().getAddress())
            .addValue("postcode", order.getOrderer().getAddress().getPostCode())
            .addValue("orderStatus", order.getOrderStatus().toString())
            .addValue("createdAt", order.getCreatedDateTime())
            .addValue("updatedAt", order.getModifiedDateTime());
    }

    private SqlParameterSource toOrderItemParameterSource(Long orderId, LocalDateTime createdDateTime,
        LocalDateTime modifiedDateTime, OrderItem orderItem) {
        return new MapSqlParameterSource()
            .addValue("orderId", orderId)
            .addValue("productId", orderItem.getProductId())
            .addValue("price", orderItem.getPrice().getAmount())
            .addValue("quantity", orderItem.getQuantity().getAmount())
            .addValue("createdAt", createdDateTime)
            .addValue("updatedAt", modifiedDateTime);
    }
}
