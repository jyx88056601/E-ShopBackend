package com.jyx.eshopbackend.service;

import com.jyx.eshopbackend.dto.OrderItemDTO;
import com.jyx.eshopbackend.dto.OrderRequestDTO;
import com.jyx.eshopbackend.dto.OrderResponseDTO;
import com.jyx.eshopbackend.model.*;
import com.jyx.eshopbackend.persistence.OrderRepository;
import com.jyx.eshopbackend.persistence.PaymentRepository;
import com.jyx.eshopbackend.persistence.ProductRepository;
import com.jyx.eshopbackend.persistence.ShipmentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

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
    public Optional<OrderResponseDTO> createOrder(OrderRequestDTO orderRequestDTO)   {
        // build a new order with auto generated UUID
        Order order = orderRepository.save(new Order());
        order.setOrderNumber(order.getId().toString() + order.getOrderTime());
        order.setOrderStatus(OrderStatus.UNPAID);
        Shipment shipment = shipmentRepository.save(new Shipment());
        order.setShipment(shipment);
        Payment payment = paymentRepository.save(new Payment());
        order.setPayment(payment);
       for (OrderItemDTO orderItemDTO : orderRequestDTO.getOrderItemRequestDTOList()) {
          String product_id = orderItemDTO.getProduct_id();
          var product = productRepository.findById(Long.parseLong(product_id)).orElseThrow(() -> new RuntimeException("No product found"));
          if (product.getStock() < Integer.parseInt(orderItemDTO.getQuantity())) {
                return Optional.empty();
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
           order.getOrderItems().add(orderItem);
       }
       return Optional.of(new OrderResponseDTO(orderRepository.save(order)));
    }
}
