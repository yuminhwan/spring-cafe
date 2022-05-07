package com.prgrms.springcafe.order.dto;

import javax.validation.constraints.NotEmpty;

import org.hibernate.validator.constraints.Length;

import com.prgrms.springcafe.order.domain.vo.Address;

public class ModifyAddressRequest {

    @NotEmpty(message = "주소는 공백만 있을 수 없습니다.")
    @Length(min = 5, max = 100, message = "주소는 {min}글자 이상 {max}글자 이하여야합니다.")
    private String address;

    @NotEmpty(message = "우편번호는 공백만 있을 수 없습니다.")
    @Length(min = 3, max = 10, message = "우변번호는 {min}글자 이상 {max}글자 이하여야합니다.")
    private String postcode;

    public ModifyAddressRequest() {
    }

    public ModifyAddressRequest(String address, String postcode) {
        this.address = address;
        this.postcode = postcode;
    }

    public Address toEntity() {
        return new Address(address, postcode);
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

}
