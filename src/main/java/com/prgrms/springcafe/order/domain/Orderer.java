package com.prgrms.springcafe.order.domain;

import java.util.Objects;

import com.prgrms.springcafe.order.domain.vo.Address;
import com.prgrms.springcafe.order.domain.vo.Email;

public class Orderer {
    private final Email email;
    private final Address address;

    public Orderer(Email email, Address address) {
        this.email = email;
        this.address = address;
    }

    public Email getEmail() {
        return email;
    }

    public Address getAddress() {
        return address;
    }

    public Orderer changeAddress(Address address) {
        return new Orderer(this.email, address);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Orderer orderer = (Orderer)o;
        return Objects.equals(getEmail(), orderer.getEmail());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getEmail());
    }
}
