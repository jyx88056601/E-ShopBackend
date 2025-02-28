package com.jyx.eshopbackend.dto;

public class OrderItemRequestDTO extends OrderItemDTO{

    private final String merchantId;
    public OrderItemRequestDTO(String productId, String quantity, String merchantID) {
        super(productId, quantity);
        this.merchantId = merchantID;
    }

    public String getMerchantId() {
        return merchantId;
    }


    public String getProductId() {
        return super.getProductId();
    }


    public String getQuantity() {
        return super.getQuantity();
    }

}
