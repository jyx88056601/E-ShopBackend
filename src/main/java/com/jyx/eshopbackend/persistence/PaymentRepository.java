package com.jyx.eshopbackend.persistence;

import com.jyx.eshopbackend.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface PaymentRepository extends JpaRepository<Payment,Long> {

    Optional<Payment> findPaymentByOrder_Id(Long orderId);
}
