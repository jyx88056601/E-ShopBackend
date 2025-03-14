package com.jyx.eshopbackend.dto;

import java.util.List;

public class AddressListDTO {
    List<String> addresses;

    public AddressListDTO(List<String> addresses) {
        this.addresses = addresses;
    }

    public List<String> getAddresses() {
        return addresses;
    }

    public void setAddresses(List<String> addresses) {
        this.addresses = addresses;
    }
}
