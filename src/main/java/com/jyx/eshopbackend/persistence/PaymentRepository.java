package com.jyx.eshopbackend.persistence;

import com.jyx.eshopbackend.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface PaymentRepository extends JpaRepository<Payment,Long> {

    Optional<Payment> findPaymentByOrder_Id(UUID orderId);
}
