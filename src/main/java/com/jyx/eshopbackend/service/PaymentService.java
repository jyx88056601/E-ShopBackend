package com.jyx.eshopbackend.service;

import com.jyx.eshopbackend.dto.PaymentResponseDTO;
import com.jyx.eshopbackend.dto.PaypalTransactionResponseDTO;
import com.jyx.eshopbackend.model.Payment;
import com.jyx.eshopbackend.model.PaymentStatus;
import com.jyx.eshopbackend.persistence.OrderRepository;
import com.jyx.eshopbackend.persistence.PaymentRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class PaymentService {

    @Value("${paypal.clientId}")
    private String clientId;

    @Value("${paypal.secretKey}")
    private String clientSecret;
    private final OrderRepository orderRepository;

    private final PaymentRepository paymentRepository;

    private final WebClient paypalWebClient;

    public PaymentService(OrderRepository orderRepository, PaymentRepository paymentRepository, WebClient paypalWebClient) {
        this.orderRepository = orderRepository;
        this.paymentRepository = paymentRepository;
        this.paypalWebClient = paypalWebClient;
    }

    private String getPayPalAccessToken() {
        String auth = clientId + ":" + clientSecret;
        String encodedAuth = new String(java.util.Base64.getEncoder().encode(auth.getBytes()));

        var response = paypalWebClient.post()
                .uri("v1/oauth2/token")
                .header(HttpHeaders.AUTHORIZATION, "Basic " + encodedAuth)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .bodyValue("grant_type=client_credentials")
                .retrieve()
                .bodyToMono(String.class)
                .block();


        String accessToken = null;
        if (response != null && response.contains("access_token")) {
            accessToken = response.split("\"access_token\":\"")[1].split("\"")[0];
        }

        return accessToken;
    }

    @Transactional
    public Optional<PaymentResponseDTO> initializePayment(String orderId,String paymentMethod) {
        var order = orderRepository.findById(UUID.fromString(orderId)).orElseThrow(()->new RuntimeException("No order found"));
        Payment payment;
        PaymentResponseDTO paymentResponseDTO;
        PaypalTransactionResponseDTO paypalTransactionResponseDTO;
        if (order.getPayment() != null) {
            payment = order.getPayment();
            paypalTransactionResponseDTO = createTransaction(getPayPalAccessToken(), String.valueOf(payment.getAmount()));
            payment.setTransactionId(paypalTransactionResponseDTO.getId());
            payment.setPaymentMethod(paymentMethod);
            paymentRepository.save(payment);
            order.setPayment(payment);
            orderRepository.save(order);
            paymentResponseDTO = new PaymentResponseDTO(order.getPayment());
            paymentResponseDTO.setPaymentMethod(payment.getPaymentMethod());
            paymentResponseDTO.setLinks(paypalTransactionResponseDTO.getLinks());
        } else {
            payment = new Payment();
            payment.setAmount(order.getTotalAmount());
            payment.setOrder(order);
            payment.setPaymentDate(LocalDateTime.now());
            paypalTransactionResponseDTO = createTransaction(getPayPalAccessToken(), String.valueOf(payment.getAmount()));
            payment.setTransactionId(paypalTransactionResponseDTO.getId());
            payment.setPaymentMethod(paymentMethod);
            payment.setStatus(PaymentStatus.PENDING);
            payment = paymentRepository.save(payment);
            order.setPayment(payment);
            orderRepository.save(order);
            paymentResponseDTO = new PaymentResponseDTO(payment);
            paymentResponseDTO.setPaymentMethod(payment.getPaymentMethod());
            paymentResponseDTO.setLinks(paypalTransactionResponseDTO.getLinks());
        }
        return Optional.of(paymentResponseDTO);
    }

    public PaypalTransactionResponseDTO createTransaction(String token, String amount) {

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

        try {
            return paypalWebClient.post()
                    .uri(url )
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .bodyValue(requestBody)
                    .retrieve()
                    .bodyToMono(PaypalTransactionResponseDTO.class)
                    .toFuture()
                    .get();

        } catch (Exception e) {
            PaypalTransactionResponseDTO response =  new PaypalTransactionResponseDTO();
            response.setStatus(e.getMessage());
          return response;
        }
    }
}
