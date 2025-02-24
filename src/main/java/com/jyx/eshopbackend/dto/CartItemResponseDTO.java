package com.jyx.eshopbackend.dto;

public class CartItemResponseDTO {


    private final String cartItemId;
    private final String quantity;

    private final ProductSimplifiedResponseDTO productSimplifiedResponseDTO;


    public CartItemResponseDTO(String cartItemId, String quantity, ProductSimplifiedResponseDTO productSimplifiedResponseDTO) {
        this.cartItemId = cartItemId;
        this.quantity = quantity;
        this.productSimplifiedResponseDTO = productSimplifiedResponseDTO;
    }

    public String getCartItemId() {
        return cartItemId;
    }

    public String getQuantity() {
        return quantity;
    }

    public ProductSimplifiedResponseDTO getProductSimplifiedResponseDTO() {
        return productSimplifiedResponseDTO;
    }
}
