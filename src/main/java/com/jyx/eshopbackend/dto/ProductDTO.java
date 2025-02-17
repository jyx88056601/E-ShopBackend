package com.jyx.eshopbackend.dto;

public class ProductDTO {

    private final String name;
    private final String price;
    private final String stock;
    private final String category;


    public ProductDTO(String name, String price, String stock, String category) {
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.category = category;
    }

    public String getStock() {
        return stock;
    }

    public String getCategory() {
        return category;
    }

    public String getPrice() {
        return price;
    }

    public String getName() {
        return name;
    }
}
