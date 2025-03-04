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
        System.out.println("Payment method: " + paymentMethod);

        return paypalService.initializePayment(orderId)
                .thenApply(paymentResponseDTO -> {
                    System.out.println("Payment Response: " + paymentResponseDTO);
                    return paymentResponseDTO;
                })
                .exceptionally(ex -> {
                    // 记录错误日志
                    System.err.println("Payment initialization failed: " + ex.getMessage());
                    // 在发生异常时返回一个带有错误信息的默认值
                    return new PaymentResponseDTO("Error" + "Payment initialization failed");
                });
    }
}
