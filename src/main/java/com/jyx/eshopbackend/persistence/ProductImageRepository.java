package com.jyx.eshopbackend.persistence;

import com.jyx.eshopbackend.model.ProductImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface ProductImageRepository extends JpaRepository<ProductImage,Long> {
    Optional<List<ProductImage>> findOrderItemsByProduct_Id(Long productId);
}
