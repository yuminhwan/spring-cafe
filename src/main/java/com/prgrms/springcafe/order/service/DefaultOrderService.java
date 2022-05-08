package com.prgrms.springcafe.order.service;

import static java.util.stream.Collectors.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.prgrms.springcafe.order.domain.Order;
import com.prgrms.springcafe.order.domain.OrderItem;
import com.prgrms.springcafe.order.domain.OrderItems;
import com.prgrms.springcafe.order.domain.OrderStatus;
import com.prgrms.springcafe.order.domain.vo.Email;
import com.prgrms.springcafe.order.dto.CreateOrderRequest;
import com.prgrms.springcafe.order.dto.ModifyAddressRequest;
import com.prgrms.springcafe.order.dto.OrderResponse;
import com.prgrms.springcafe.order.exception.OrderCanNotModifiedException;
import com.prgrms.springcafe.order.exception.OrderNotFoundException;
import com.prgrms.springcafe.order.exception.ProductOutOfStockExcpetion;
import com.prgrms.springcafe.order.repository.OrderRepository;
import com.prgrms.springcafe.product.domain.Product;
import com.prgrms.springcafe.product.exception.ProductNotFoundException;
import com.prgrms.springcafe.product.repository.ProductRepostiory;
import com.prgrms.springcafe.vo.Quantity;

@Service
@Transactional
public class DefaultOrderService implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemService orderItemService;
    private final ProductRepostiory productRepostiory;

    public DefaultOrderService(OrderRepository orderRepository, OrderItemService orderItemService,
        ProductRepostiory productRepostiory) {
        this.orderRepository = orderRepository;
        this.orderItemService = orderItemService;
        this.productRepostiory = productRepostiory;
    }

    @Override
    public OrderResponse createOrder(CreateOrderRequest orderRequest) {
        Order order = orderRequest.toEntity();
        reduceProductStock(order);

        Order savedOrder = orderRepository.insert(order);
        return OrderResponse.from(savedOrder);
    }

    private void reduceProductStock(Order order) {
        OrderItems orderItems = order.getOrderItems();
        Map<Long, Quantity> quantityMap = orderItems.mapToProductAndQuantity();
        for (Long productId : quantityMap.keySet()) {
            Product product = findProduct(productId);

            Quantity soldQuantity = quantityMap.get(productId);
            validateProduct(product, soldQuantity);

            product.sellProduct(soldQuantity);
            productRepostiory.update(product);
        }

    }

    private void validateProduct(Product product, Quantity soldQuantity) {
        if (product.isNotSellable(soldQuantity)) {
            throw new ProductOutOfStockExcpetion(product.getId());
        }
    }

    @Override
    public void changeAddress(Long id, ModifyAddressRequest addressRequest) {
        Order order = findOrder(id);
        validateModifiable(order);

        order.changeAddress(addressRequest.toEntity());
        orderRepository.updateAddress(order);
    }

    @Override
    public void changeOrderItem(Long orderId, Long orderItemId, Quantity quantity) {
        Order order = findOrder(orderId);
        validateModifiable(order);

        OrderItem orderItem = order.getOrderItems().findOrderItem(orderItemId);
        Product product = findProduct(orderItem.getProductId());

        updateProduct(quantity, orderItem, product);

        if (quantity.isZero()) {
            removeOrderItem(orderId, orderItemId);
            return;
        }
        orderItemService.changeOrderItem(orderItem.getId(), quantity);
    }

    private void validateModifiable(Order order) {
        if (order.isNotModifiable()) {
            throw new OrderCanNotModifiedException(order.getId());
        }
    }

    private void updateProduct(Quantity quantity, OrderItem orderItem, Product product) {
        if (orderItem.isLowerQuantity(quantity)) {
            Quantity update = quantity.minus(orderItem.getQuantity());
            validateProduct(product, update);
            product.sellProduct(update);
            productRepostiory.update(product);
            return;
        }
        Quantity update = orderItem.getQuantity().minus(quantity);
        product.plusProduct(update);
        productRepostiory.update(product);
    }

    @Override
    public void removeOrderItem(Long orderId, Long orderItemId) {
        Order order = findOrder(orderId);
        validateModifiable(order);
        OrderItem orderItem = order.getOrderItems().findOrderItem(orderItemId);

        Product product = findProduct(orderItem.getProductId());
        product.plusProduct(orderItem.getQuantity());
        productRepostiory.update(product);

        if (order.hasOneItem()) {
            order.cancel();
            changeStatus(orderId, order.getOrderStatus());
            return;
        }

        orderItemService.removeOrderItem(orderItemId);
    }

    private Product findProduct(Long orderItem) {
        return productRepostiory.findById(orderItem)
            .orElseThrow(() -> new ProductNotFoundException(orderItem));
    }

    @Override
    public void changeStatus(Long id, OrderStatus orderStatus) {
        Order order = findOrder(id);
        orderStatus.changeStatus(order);
        orderRepository.updateStatus(order);
    }

    @Transactional(readOnly = true)
    @Override
    public List<OrderResponse> findOrders(Optional<Email> email) {
        List<Order> orders = email.map(orderRepository::findByEmail)
            .orElseGet(orderRepository::findAll);

        return orders.stream()
            .map(OrderResponse::from)
            .collect(toList());
    }

    @Transactional(readOnly = true)
    @Override
    public OrderResponse findOrderById(Long id) {
        Order order = findOrder(id);
        return OrderResponse.from(order);
    }

    private Order findOrder(Long id) {
        return orderRepository.findById(id)
            .orElseThrow(() -> new OrderNotFoundException(id));
    }
}
