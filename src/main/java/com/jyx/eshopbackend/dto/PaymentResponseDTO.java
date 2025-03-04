package com.jyx.eshopbackend.dto;

import com.jyx.eshopbackend.model.Payment;

import java.util.List;

public class PaymentResponseDTO {
    private String paymentId;
    private String amount;
    private String paymentDate;

    private final String paymentStatus;

    private String paymentMethod;

    List<PaypalTransactionResponseDTO.Link> links;

    public PaymentResponseDTO(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public PaymentResponseDTO(Payment payment) {
        this.paymentId = String.valueOf(payment.getId());
        this.amount = String.valueOf(payment.getAmount());
        this.paymentDate = String.valueOf(payment.getPaymentDate());
        this.paymentStatus = String.valueOf(payment.getStatus());
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }


    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public List<PaypalTransactionResponseDTO.Link> getLinks() {
        return links;
    }

    public void setLinks(List<PaypalTransactionResponseDTO.Link> links) {
        this.links = links;
    }

    public String getPaymentId() {
        return paymentId;
    }

    public String getAmount() {
        return amount;
    }

    public String getPaymentDate() {
        return paymentDate;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }


}
