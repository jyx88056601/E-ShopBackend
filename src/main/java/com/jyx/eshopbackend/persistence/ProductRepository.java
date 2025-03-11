package com.jyx.eshopbackend.persistence;

import com.jyx.eshopbackend.model.Product;
import io.micrometer.common.lang.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long>, ProductCriteriaRepository, JpaSpecificationExecutor<Product> {
//    Page<Product> findByOwnerId(Long ownerId, Pageable pageable);

    @Query("SELECT p FROM Product p WHERE p.id IN :productIds")
    Page<Product> findByIdIn(@Param("productIds") List<Long> productIds, Pageable pageable);
    @Override
    @NonNull
    Page<Product> findAll(@NonNull Pageable pageable);

}