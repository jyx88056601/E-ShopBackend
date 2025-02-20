package com.jyx.eshopbackend.dto;

import java.util.List;

public class ProductUploadDTO extends ProductDTO {

    private final String description;

    private final List<String> images;

    private final String id;

    public ProductUploadDTO(String name, String price, String stock, String category, String description, List<String> images, String id) {
         super(name, price,stock,category);
        this.description = description;
        this.images = images;
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public String getName() {
       return super.getName();
    }

    public String getPrice() {
        return super.getPrice();
    }

    public String getStock() {
        return super.getStock();
    }

    public String getCategory() {
        return super.getCategory();
    }

    public String getDescription() {
        return description;
    }

    public List<String> getImages() {
        return images;
    }
}
