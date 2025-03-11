package com.jyx.eshopbackend.service;

import com.amazonaws.services.kms.model.NotFoundException;
import com.jyx.eshopbackend.dto.PaymentResponseDTO;
import com.jyx.eshopbackend.model.OrderStatus;
import com.jyx.eshopbackend.model.PaymentStatus;
import com.jyx.eshopbackend.persistence.OrderRepository;
import com.jyx.eshopbackend.persistence.PaymentRepository;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Service
public class PaymentService {

    private final PaypalService paypalService;
    private final PaymentRepository paymentRepository;

    private final OrderService orderService;
    private final OrderRepository orderRepository;

    public PaymentService(PaypalService paypalService,
                          PaymentRepository paymentRepository, OrderService orderService,
                          OrderRepository orderRepository) {
        this.paypalService = paypalService;
        this.paymentRepository = paymentRepository;
        this.orderService = orderService;
        this.orderRepository = orderRepository;
    }


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

    @Async
    public CompletableFuture<PaymentResponseDTO> verifyPayment(String orderId, String paymentMethod) {
        var payment = paymentRepository.findPaymentByOrder_Id(UUID.fromString(orderId))
                .orElseThrow(() -> new NotFoundException("No payment found with orderId = " + orderId));
        var order = orderService.findOrderById(orderId).orElseThrow(() -> new NotFoundException("No order found with id = " + orderId));
        try {
            return paypalService.verifyTransaction(payment.getTransactionId())
                    .thenApply(paypalVerificationResponseDTO ->{
                        payment.setStatus(PaymentStatus.COMPLETED);
                        payment.setPaymentMethod(paymentMethod);
                        order.setOrderStatus(OrderStatus.PAID);
                        payment.setOrder(order);
                        order.setPayment(payment);
                        paymentRepository.save(payment);
                        orderService.updateOrder(order);
                        return new PaymentResponseDTO(paypalVerificationResponseDTO.getStatus());}
                    )
                    .exceptionally(ex -> {
                      throw new RuntimeException(ex.getMessage());
                    });
        } catch (Exception e) {
             throw new RuntimeException(e.getMessage());
        }
    }

    public boolean isPaid(String orderId) {
         var order = orderRepository.findById(UUID.fromString(orderId)).orElseThrow(() -> new RuntimeException("No order found with id = " + orderId));
         return order.getPayment().getStatus().equals(PaymentStatus.COMPLETED);
    }
}
