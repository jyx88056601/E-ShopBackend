package com.jyx.eshopbackend.dto;

public class CartItemResponseDTO {

    private final String quantity;

    private final ProductSimplifiedResponseDTO productSimplifiedResponseDTO;


    public CartItemResponseDTO(String quantity, ProductSimplifiedResponseDTO productSimplifiedResponseDTO) {
        this.quantity = quantity;

        this.productSimplifiedResponseDTO = productSimplifiedResponseDTO;
    }


    public String getQuantity() {
        return quantity;
    }

    public ProductSimplifiedResponseDTO getProductSimplifiedResponseDTO() {
        return productSimplifiedResponseDTO;
    }
}
