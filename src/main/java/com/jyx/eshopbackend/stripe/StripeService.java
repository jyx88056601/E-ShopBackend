package com.jyx.eshopbackend.stripe;


import com.amazonaws.services.kms.model.NotFoundException;
import com.jyx.eshopbackend.model.*;
import com.jyx.eshopbackend.persistence.OrderRepository;
import com.jyx.eshopbackend.persistence.PaymentRepository;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class StripeService {

    private static final String YOUR_DOMAIN = "http://localhost:5173";
    private final OrderRepository orderRepository;

    private  final PaymentRepository paymentRepository;

    public StripeService(OrderRepository orderRepository, PaymentRepository paymentRepository) {
        this.orderRepository = orderRepository;
        this.paymentRepository = paymentRepository;
    }

    @Transactional
    public Map<String, String> createCheckoutSession(Order order, Payment payment) {
        var orderItems = order.getOrderItems();
        try {
            SessionCreateParams.Builder paramsBuilder = SessionCreateParams.builder()
                    .setUiMode(SessionCreateParams.UiMode.EMBEDDED)
                    .setMode(SessionCreateParams.Mode.PAYMENT)
                    .setReturnUrl(YOUR_DOMAIN + "/personal/return?session_id={CHECKOUT_SESSION_ID}");

            for (OrderItem item : orderItems) {
                paramsBuilder.addLineItem(
                        SessionCreateParams.LineItem.builder()
                                .setQuantity((long) item.getQuantity())
                                .setPriceData(
                                        SessionCreateParams.LineItem.PriceData.builder()
                                                .setCurrency("usd")
                                                .setUnitAmount(item.getPrice().multiply(new BigDecimal("100")).longValue())
                                                .setProductData(
                                                        SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                                                .setName(item.getProductName())
                                                                .build()
                                                )
                                                .build()
                                )
                                .build()
                );
            }
            Session session = Session.create(paramsBuilder.build());
            Map<String, String> response = new HashMap<>();
            response.put("clientSecret", session.getRawJsonObject()
                    .getAsJsonPrimitive("client_secret")
                    .getAsString());
            response.put("sessionId", session.getId());
            payment.setStatus(PaymentStatus.PENDING);
            order.setPayment(paymentRepository.save(payment));
            orderRepository.save(order);
            return response;
        } catch (Exception e) {
            throw new RuntimeException("Failed to create checkout session", e);
        }
    }


    @Transactional
    public Map<String, String> getSessionStatus(String sessionId,String orderId) {
        try {
            Session session = Session.retrieve(sessionId);
            Map<String, String> response = new HashMap<>();
            response.put("status", session.getRawJsonObject()
                    .getAsJsonPrimitive("status")
                    .getAsString());
            response.put("customer_email", session.getRawJsonObject()
                    .getAsJsonObject("customer_details")
                    .getAsJsonPrimitive("email")
                    .getAsString());
            if (response.get("status").equals("complete")) {
                var order = orderRepository.findById(UUID.fromString(orderId)).orElseThrow(() -> new NotFoundException("No order found"));
                var payment = order.getPayment();
                payment.setStatus(PaymentStatus.COMPLETED);
                payment.setTransactionId(sessionId);
                order.setOrderStatus(OrderStatus.PAID);
                order.setPayment(paymentRepository.save(payment));
            }
            return response;
        } catch (Exception e) {
            throw new RuntimeException("Failed to retrieve session status", e);
        }
    }
}