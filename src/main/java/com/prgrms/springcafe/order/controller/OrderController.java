package com.prgrms.springcafe.order.controller;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.prgrms.springcafe.order.domain.OrderStatus;
import com.prgrms.springcafe.order.dto.ModifyAddressRequest;
import com.prgrms.springcafe.order.dto.OrderResponse;
import com.prgrms.springcafe.order.service.OrderService;

@Controller
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/orders")
    public String showOrdersPage(Model model) {
        List<OrderResponse> orders = orderService.findOrders(Optional.empty());
        model.addAttribute("orders", orders);
        return "order/order-list";
    }

    @GetMapping("/orders/{orderId}")
    public String showOrderDetails(@PathVariable Long orderId, Model model) {
        OrderResponse order = orderService.findOrderById(orderId);
        model.addAttribute("order", order);
        return "order/order-details";
    }

    @PostMapping("/orders/{orderId}/orderer")
    public String showModifyOrdererPage(@ModelAttribute("order") ModifyAddressRequest modifyAddressRequest,
        @ModelAttribute("orderId") @PathVariable Long orderId) {
        return "order/modify-orderer";
    }

    @PostMapping("/orders/{orderId}/modify/orderer")
    public String modifyOrderer(@ModelAttribute("order") @Valid ModifyAddressRequest modifyAddressRequest,
        BindingResult bindingResult, @ModelAttribute("orderId") @PathVariable Long orderId) {

        if (bindingResult.hasErrors()) {
            return "order/modify-orderer";
        }

        orderService.changeAddress(orderId, modifyAddressRequest);
        return "redirect:/orders/" + orderId;
    }

    @PostMapping("/orders/{orderId}/status")
    public String showModifyOrderStatusPage(@ModelAttribute("orderStatus") OrderStatus orderStatus,
        @ModelAttribute("orderId") @PathVariable Long orderId, Model model) {
        model.addAttribute("orderStatus", OrderStatus.toStringAll());
        return "order/modify-order-status";
    }

    @PostMapping("/orders/{orderId}/modify/status")
    public String modifyStatus(OrderStatus orderStatus, @PathVariable Long orderId) {
        orderService.changeStatus(orderId, orderStatus);
        return "redirect:/orders/" + orderId;
    }
}
