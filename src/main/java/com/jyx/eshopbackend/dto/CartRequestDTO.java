package com.jyx.eshopbackend.dto;

import java.util.List;

public class CartRequestDTO{
    private final String cartId;

    public CartRequestDTO(String cartId, List<CartItemRequestDTO> cartItemRequestDTO) {
        this.cartId = cartId;
        this.cartItemRequestDTO = cartItemRequestDTO;
    }

    public String getCartId() {
        return cartId;
    }

    private final List<CartItemRequestDTO> cartItemRequestDTO;

    public List<CartItemRequestDTO> getProductRequestDTO() {
        return cartItemRequestDTO;
    }
}
