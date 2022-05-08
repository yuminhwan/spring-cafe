package com.prgrms.springcafe.order.controller;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.prgrms.springcafe.order.domain.OrderStatus;
import com.prgrms.springcafe.order.domain.vo.Email;
import com.prgrms.springcafe.order.dto.CreateOrderRequest;
import com.prgrms.springcafe.order.dto.ModifyAddressRequest;
import com.prgrms.springcafe.order.dto.OrderResponse;
import com.prgrms.springcafe.order.service.OrderService;
import com.prgrms.springcafe.vo.Quantity;

@RestController
@RequestMapping("api/v1")
public class OrderRestController {

    private final OrderService orderService;

    public OrderRestController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/orders")
    public ResponseEntity<List<OrderResponse>> getOrders(@RequestParam Optional<Email> email) {
        return ResponseEntity.ok(orderService.findOrders(email));
    }

    @GetMapping("/orders/{orderId}")
    public ResponseEntity<OrderResponse> getOrders(@PathVariable Long orderId) {
        return ResponseEntity.ok(orderService.findOrderById(orderId));
    }

    @PostMapping("/orders")
    public ResponseEntity<OrderResponse> createOrder(@Valid @RequestBody CreateOrderRequest orderRequest) {
        OrderResponse order = orderService.createOrder(orderRequest);
        return ResponseEntity.created(URI.create("/orders/" + order.getId())).body(order);
    }

    @PutMapping("/orders/{orderId}/orderer")
    public ResponseEntity<Void> modifyOrderer(@PathVariable Long orderId,
        @Valid @RequestBody ModifyAddressRequest modifyAddressRequest) {
        orderService.changeAddress(orderId, modifyAddressRequest);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/orders/{orderId}/status")
    public ResponseEntity<Void> modifyOrderStatus(@PathVariable Long orderId, @RequestParam OrderStatus orderStatus) {
        orderService.changeStatus(orderId, orderStatus);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/orders/{orderId}/items/{orderItemId}")
    public ResponseEntity<Void> modifyOrderItem(@PathVariable Long orderItemId, @PathVariable Long orderId,
        @RequestParam int quantity) {
        orderService.changeOrderItem(orderId, orderItemId, new Quantity(quantity));
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/orders/{orderId}/items/{orderItemId}")
    public ResponseEntity<Void> removeOrderItem(@PathVariable Long orderId, @PathVariable Long orderItemId) {
        orderService.removeOrderItem(orderId, orderItemId);
        return ResponseEntity.noContent().build();
    }
}
