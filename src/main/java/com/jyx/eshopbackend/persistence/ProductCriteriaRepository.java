package com.jyx.eshopbackend.persistence;

import com.jyx.eshopbackend.model.Product;

import java.math.BigDecimal;
import java.util.List;

public interface ProductCriteriaRepository {
    List<Product> findProductsByCriteria(String name, BigDecimal minPrice, BigDecimal maxPrice);
}
