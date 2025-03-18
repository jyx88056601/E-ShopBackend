package com.jyx.eshopbackend.persistence;

import com.jyx.eshopbackend.model.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface OrderRepository extends JpaRepository<Order, UUID> {

    List<Order> findByMerchantId(Long merchantId);

    List<Order> findByCustomerId(Long customerId);

    Page<Order> findByMerchantIdAndCustomerId(Long merchantId, Long customerId, Pageable pageable);

    Page<Order> findByMerchantId(Long merchantId, Pageable pageable);

    Page<Order> findByCustomerId(Long customerId, Pageable pageable);

}
