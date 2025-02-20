package com.jyx.eshopbackend.dto;

import com.jyx.eshopbackend.model.Cart;
import com.jyx.eshopbackend.model.CartItem;

import java.util.List;

public class CartDTO {

    private final Long ownerId;
    private final List<CartItem> cartItemList;
    public CartDTO(Cart cart) {
        cartItemList = cart.getCartItems();
        this.ownerId = cart.getId();
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public List<CartItem> getCartItemList() {
        return cartItemList;
    }
}
