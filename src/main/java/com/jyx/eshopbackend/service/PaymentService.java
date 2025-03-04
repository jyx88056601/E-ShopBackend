package com.jyx.eshopbackend.service;

import com.jyx.eshopbackend.dto.PaymentResponseDTO;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
public class PaymentService {

    private final PaypalService paypalService;

    public PaymentService(PaypalService paypalService) {
        this.paypalService = paypalService;
    }

    // 创建支付
    @Async
    public CompletableFuture<PaymentResponseDTO> createPayment(String orderId, String paymentMethod) {
        return paypalService.initializePayment(orderId)
                .thenApply(paymentResponseDTO -> {
                    System.out.println("Payment Response: " + paymentResponseDTO);
                    return paymentResponseDTO;
                })
                .exceptionally(ex -> {
                    System.err.println("Payment initialization failed: " + ex.getMessage());
                    return new PaymentResponseDTO("Error" + "Payment initialization failed");
                });
    }
}
