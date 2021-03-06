package com.prgrms.springcafe.order.dto;

import static com.prgrms.springcafe.order.domain.OrderStatus.*;
import static java.util.stream.Collectors.*;

import java.util.List;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import com.prgrms.springcafe.order.domain.Order;
import com.prgrms.springcafe.order.domain.OrderItem;

public class CreateOrderRequest {

    @NotEmpty(message = "이메일은 공백만 있을 수 없습니다.")
    @Email(message = "이메일 형식이 아닙니다.")
    private String email;

    @NotEmpty(message = "주소는 공백만 있을 수 없습니다.")
    @Length(min = 5, max = 100, message = "주소는 {min}글자 이상 {max}글자 이하여야합니다.")
    private String address;

    @NotEmpty(message = "우편번호는 공백만 있을 수 없습니다.")
    @Length(min = 3, max = 10, message = "우변번호는 {min}글자 이상 {max}글자 이하여야합니다.")
    private String postcode;

    @NotNull(message = "주문 상품들은 비어있을 수 없습니다.")
    private List<CreateOrderItemRequest> orderItems;

    public CreateOrderRequest() {
    }

    public CreateOrderRequest(String email, String address, String postcode,
        List<CreateOrderItemRequest> orderItems) {
        this.email = email;
        this.address = address;
        this.postcode = postcode;
        this.orderItems = orderItems;
    }

    public Order toEntity() {
        return Order.of(email, address, postcode, orderItems(), ACCEPTED);
    }

    private List<OrderItem> orderItems() {
        return orderItems.stream()
            .map(CreateOrderItemRequest::toEntity)
            .collect(toList());
    }

    public String getEmail() {
        return email;
    }

    public String getAddress() {
        return address;
    }

    public String getPostcode() {
        return postcode;
    }

    public List<CreateOrderItemRequest> getOrderItems() {
        return orderItems;
    }

}
