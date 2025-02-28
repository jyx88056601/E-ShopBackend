package com.jyx.eshopbackend.service;

import com.jyx.eshopbackend.dto.OrderItemDTO;
import com.jyx.eshopbackend.dto.OrderRequestDTO;
import com.jyx.eshopbackend.dto.OrderResponseDTO;
import com.jyx.eshopbackend.model.*;
import com.jyx.eshopbackend.persistence.OrderRepository;
import com.jyx.eshopbackend.persistence.PaymentRepository;
import com.jyx.eshopbackend.persistence.ProductRepository;
import com.jyx.eshopbackend.persistence.ShipmentRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class OrderService {
   private final OrderRepository orderRepository;

   private final ProductRepository productRepository;
   private final ShipmentRepository shipmentRepository;

   private final PaymentRepository paymentRepository;

    public OrderService(OrderRepository orderRepository, ProductRepository productRepository, ShipmentRepository shipmentRepository, PaymentRepository paymentRepository) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
        this.shipmentRepository = shipmentRepository;
        this.paymentRepository = paymentRepository;
    }

    @Transactional
    public Optional<OrderResponseDTO> createOrder(String customerId, String merchantId, OrderRequestDTO orderRequestDTO)   {
        Order order = new Order();
        order.setOrderNumber(customerId + LocalDateTime.now() + merchantId);
        order.setOrderStatus(OrderStatus.UNPAID);
        order.setCustomerId(Long.parseLong(customerId));
        order.setMerchantId(Long.parseLong(merchantId));
        BigDecimal totalAmount = new BigDecimal(0);
        List<OrderItem> orderItems = new ArrayList<>();
       for (OrderItemDTO orderItemDTO : orderRequestDTO.getOrderItemRequestDTOList()) {
          String product_id = orderItemDTO.getProductId();
          var product = productRepository.findById(Long.parseLong(product_id)).orElseThrow(() -> new RuntimeException("No product found"));
          if (product.getStock() < Integer.parseInt(orderItemDTO.getQuantity())) {
               throw new IllegalArgumentException(product.getName() + " is out of stock");
          }
           int quantity = Integer.parseInt(orderItemDTO.getQuantity());
           product.setStock(product.getStock() - quantity );
           productRepository.save(product);
           OrderItem orderItem = new OrderItem();
           orderItem.setOrder(order);
           orderItem.setProductId(product.getId());
           orderItem.setPrice(product.getPrice());
           orderItem.setQuantity(quantity);
           orderItem.setProductName(product.getName());
           orderItem.setOrder(order);
           orderItems.add(orderItem);
           totalAmount = totalAmount.add(BigDecimal.valueOf(orderItem.getQuantity()).multiply(orderItem.getPrice()));
       }
        order.setOrderItems(orderItems);
        order.setTotalAmount(totalAmount);
        order = orderRepository.save(order);
//        Shipment shipment = shipmentRepository.save(new Shipment(order));
//        order.setShipment(shipment);
//        Payment payment = paymentRepository.save(new Payment(order));
//        order.setPayment(payment);
        OrderResponseDTO orderResponseDTO = new OrderResponseDTO(order);
       return Optional.of(orderResponseDTO);
    }

    public Page<OrderResponseDTO> fetchOrderByCustomerId(Long customerId, int page, int size) {
            Pageable pageable = PageRequest.of(page, size);
            Page<Order> orderPage = orderRepository.findByCustomerId(customerId, pageable);
            return orderPage.map(OrderResponseDTO::new);
    }
    public void removeOrder(UUID orderId) {
        orderRepository.deleteById(orderId);
    }
}
