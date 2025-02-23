package com.jyx.eshopbackend.dto;

import java.util.List;

public class CartResponseDTO{
     private final String id; // userId
     private final List<CartItemResponseDTO> cartItemResponseDTOList;

    public CartResponseDTO(String id, List<CartItemResponseDTO> cartItemResponseDTOList) {
        this.id = id;
        this.cartItemResponseDTOList = cartItemResponseDTOList;
    }

    public String getId() {
        return id;
    }

    public List<CartItemResponseDTO> getCartItemResponseDTOList() {
        return cartItemResponseDTOList;
    }
}
