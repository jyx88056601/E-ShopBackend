package com.jyx.eshopbackend.persistence;

import com.jyx.eshopbackend.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment,Long> {

    Optional<Payment> findPaymentByOrder_Id(Long orderId);
}
