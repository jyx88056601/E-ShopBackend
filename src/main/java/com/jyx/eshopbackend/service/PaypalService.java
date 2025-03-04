package com.jyx.eshopbackend.service;

import com.jyx.eshopbackend.dto.PaymentResponseDTO;
import com.jyx.eshopbackend.dto.PaypalTransactionResponseDTO;
import com.jyx.eshopbackend.model.Order;
import com.jyx.eshopbackend.model.Payment;
import com.jyx.eshopbackend.model.PaymentStatus;
import com.jyx.eshopbackend.persistence.OrderRepository;
import com.jyx.eshopbackend.persistence.PaymentRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDateTime;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Service
public class PaypalService {

    @Value("${paypal.clientId}")
    private String clientId;

    @Value("${paypal.secretKey}")
    private String clientSecret;

    private final WebClient paypalWebClient;
    private final OrderRepository orderRepository;
    private final PaymentRepository paymentRepository;

    public PaypalService(WebClient paypalWebClient, OrderRepository orderRepository, PaymentRepository paymentRepository) {
        this.paypalWebClient = paypalWebClient;
        this.orderRepository = orderRepository;
        this.paymentRepository = paymentRepository;
    }

    @Async
    public CompletableFuture<String> getPayPalAccessToken() {

        String auth = clientId + ":" + clientSecret;
        String encodedAuth = new String(java.util.Base64.getEncoder().encode(auth.getBytes()));
        return paypalWebClient.post()
                .uri("v1/oauth2/token")
                .header(HttpHeaders.AUTHORIZATION, "Basic " + encodedAuth)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .bodyValue("grant_type=client_credentials")
                .retrieve()
                .bodyToMono(String.class)
                .toFuture()
                .thenApply(response -> {
                    if (response != null && response.contains("access_token")) {
                        return response.split("\"access_token\":\"")[1].split("\"")[0];
                    }
                    throw new RuntimeException("Failed to retrieve access token from PayPal");
                })
                .exceptionally(throwable -> {
                    throw new RuntimeException("Error retrieving PayPal access token", throwable);
                });
    }


    @Async
    public CompletableFuture<PaymentResponseDTO> initializePayment(String orderId) {

        var order = orderRepository.findById(UUID.fromString(orderId))
                .orElseThrow(() -> new RuntimeException("Order not found"));

        Payment payment = paymentRepository.findPaymentByOrder_Id(UUID.fromString(orderId))
                .orElseGet(() -> {
                    Payment newPayment = initializePayment(order);
                    paymentRepository.save(newPayment);
                    return newPayment;
                });


        return getPayPalAccessToken()
                .thenCompose(token -> createTransaction(token, String.valueOf(payment.getAmount())))
                .thenApply(paypalTransactionResponseDTO -> {

                    payment.setTransactionId(paypalTransactionResponseDTO.getId());
                    payment.setStatus(PaymentStatus.PENDING);
                    payment.setPaymentDate(LocalDateTime.now());
                    paymentRepository.save(payment);
                    order.setPayment(payment);
                    orderRepository.save(order);

                    PaymentResponseDTO paymentResponseDTO = new PaymentResponseDTO(payment);
                    paymentResponseDTO.setPaymentMethod(payment.getPaymentMethod());
                    paymentResponseDTO.setLinks(paypalTransactionResponseDTO.getLinks());
                    return paymentResponseDTO;
                })
                .exceptionally(throwable -> {
                    throw new RuntimeException("Error during PayPal payment initialization", throwable);
                });
    }


    private Payment initializePayment(Order order) {
        Payment payment = new Payment();
        payment.setAmount(order.getTotalAmount());
        payment.setOrder(order);
        return payment;
    }

    @Async
    public CompletableFuture<PaypalTransactionResponseDTO> createTransaction(String token, String amount) {
        String requestBody = "{" +
                "\"intent\": \"AUTHORIZE\"," +
                "\"purchase_units\": [{" +
                "\"reference_id\": \"" + UUID.randomUUID() + "\"," +
                "\"amount\": {" +
                "\"currency_code\": \"USD\"," +
                "\"value\": \"" + amount + "\"" +
                "}" +
                "}]," +
                "\"payment_source\": {" +
                "\"paypal\": {" +
                "\"address\": {" +
                "\"address_line_1\": \"2211 N First Street\"," +
                "\"address_line_2\": \"17.3.160\"," +
                "\"admin_area_1\": \"CA\"," +
                "\"admin_area_2\": \"San Jose\"," +
                "\"postal_code\": \"95131\"," +
                "\"country_code\": \"US\"" +
                "}," +
                "\"email_address\": \"payer@example.com\"," +
                "\"payment_method_preference\": \"IMMEDIATE_PAYMENT_REQUIRED\"," +
                "\"experience_context\": {" +
                "\"return_url\": \"https://example.com/returnUrl\"," +
                "\"cancel_url\": \"https://example.com/cancelUrl\"" +
                "}" +
                "}" +
                "}" +
                "}";

        String url = "/v2/checkout/orders";

        return paypalWebClient.post()
                .uri(url)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(PaypalTransactionResponseDTO.class)
                .toFuture()
                .thenApply(paypalTransactionResponseDTO -> {
                    return paypalTransactionResponseDTO;
                })
                .exceptionally(throwable -> {
                    throw new RuntimeException("Error creating PayPal transaction", throwable);
                });
    }


    @Async
    public CompletableFuture<PaypalTransactionResponseDTO> verifyTransaction(String transactionId) {
        return getPayPalAccessToken()
                .thenCompose(token -> paypalWebClient.post()
                        .uri("/v2/checkout/orders/" + transactionId + "/capture")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .retrieve()
                        .bodyToMono(PaypalTransactionResponseDTO.class)
                        .toFuture())
                .exceptionally(throwable -> {
                    throw new RuntimeException("Transaction verification failed for ID: " + transactionId, throwable);
                });
    }
}
