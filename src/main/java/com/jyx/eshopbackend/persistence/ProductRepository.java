package com.jyx.eshopbackend.persistence;

import com.jyx.eshopbackend.model.Product;
import jakarta.annotation.Nonnull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    Page<Product> findByOwnerId(Long ownerId, Pageable pageable);

    @Query("SELECT p FROM Product p WHERE p.id IN :productIds")
    Page<Product> findByIdIn(@Param("productIds") List<Long> productIds, Pageable pageable);

    Page<Product> findAll(@Nonnull Pageable pageable);

}