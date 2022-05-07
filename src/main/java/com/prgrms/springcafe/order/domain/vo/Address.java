package com.prgrms.springcafe.order.domain.vo;

import java.util.Objects;

import com.prgrms.springcafe.global.error.exception.InvalidValueException;

public class Address {
    private static final int MAX_ADDRESS_LENGTH = 100;
    private static final int MAX_POSTCODE_LENGTH = 10;

    private final String address;
    private final String postCode;

    public Address(String address, String postCode) {
        this.address = address;
        this.postCode = postCode;
        validateAddress(address);
        validatePostcode(address, postCode);
    }

    private void validatePostcode(String address, String postCode) {
        if (Objects.isNull(postCode) || address.isBlank()) {
            throw new InvalidValueException("우편번호가 비어있습니다.");
        }

        if (postCode.length() > MAX_POSTCODE_LENGTH) {
            throw new InvalidValueException("우편번호는 10자를 초과할 수 없습니다.");
        }
    }

    private void validateAddress(String address) {
        if (Objects.isNull(address) || address.isBlank()) {
            throw new InvalidValueException("주소가 비어있습니다.");
        }

        if (address.length() > MAX_ADDRESS_LENGTH) {
            throw new InvalidValueException("주소는 100자를 초과할 수 없습니다.");
        }
    }

    public String getAddress() {
        return address;
    }

    public String getPostCode() {
        return postCode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Address address1 = (Address)o;
        return getAddress().equals(address1.getAddress()) && getPostCode().equals(address1.getPostCode());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getAddress(), getPostCode());
    }
}
