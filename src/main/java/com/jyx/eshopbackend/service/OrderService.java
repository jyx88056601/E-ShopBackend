package com.jyx.eshopbackend.service;

import com.amazonaws.services.kms.model.NotFoundException;
import com.jyx.eshopbackend.dto.OrderDetailDTO;
import com.jyx.eshopbackend.dto.OrderItemDTO;
import com.jyx.eshopbackend.dto.OrderRequestDTO;
import com.jyx.eshopbackend.dto.OrderResponseDTO;
import com.jyx.eshopbackend.model.Order;
import com.jyx.eshopbackend.model.OrderItem;
import com.jyx.eshopbackend.model.OrderStatus;
import com.jyx.eshopbackend.persistence.OrderRepository;
import com.jyx.eshopbackend.persistence.PaymentRepository;
import com.jyx.eshopbackend.persistence.ProductRepository;
import com.jyx.eshopbackend.persistence.ShipmentRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
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

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public Optional<OrderResponseDTO> createOrder(String customerId, String merchantId, OrderRequestDTO orderRequestDTO)   {
        Order order = new Order();
        order.setOrderNumber(customerId + "-" + LocalDateTime.now() + "-" + merchantId);
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
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Order.desc("orderCreatedTime")));
        Page<Order> orderPage = orderRepository.findByCustomerId(customerId, pageable);
        return orderPage.map(OrderResponseDTO::new);
    }
    public void removeOrder(UUID orderId) {
        orderRepository.deleteById(orderId);
    }

    public Optional<Order> findOrderById(String orderId) {
         return orderRepository.findById(UUID.fromString(orderId));
    }

    @Transactional
    public Optional<OrderDetailDTO> fetchOrderDetails(String orderId) {
        var order = findOrderById(orderId).orElseThrow(() -> new NotFoundException("No order found"));
        OrderDetailDTO orderDetailDTO = new OrderDetailDTO(order);
        List<OrderItem> orderItemList = order.getOrderItems();
        StringBuilder orderItems = new StringBuilder();
        for (var orderItem : orderItemList) {


            var product = productRepository.findById(orderItem.getProductId())
                    .orElseThrow(() -> new NotFoundException("No order found"));
            orderItems.append(product.getMainPictureUrl().replace("https://e-commerce-shop-ethan-jiang.s3.us-west-2.amazonaws.com/",
                            "https://e-commerce-shop-ethan-jiang.s3.us-west-2.amazonaws.com/product/"))
                    .append("#")
                    .append(product.getName())
                    .append("#")
                    .append(orderItem.getQuantity())
                    .append("#")
                    .append(orderItem.getPrice())
                    .append("#")
                    .append(orderItem.getTotalPrice())
                    .append("~");
        }

        // Remove the last '~' if present
        if (!orderItems.isEmpty() && orderItems.charAt(orderItems.length() - 1) == '~') {
            orderItems.deleteCharAt(orderItems.length() - 1);
        }
        // set up payment details if there is a record
        orderDetailDTO.setOrderItems(orderItems.toString());
        String paymentDetail = paymentRepository.findPaymentByOrder_Id(UUID.fromString(orderId))
                .map(payment -> payment.getPaymentDate() + "#" + payment.getTransactionId() +
                        "#" + payment.getStatus() + "#" + payment.getPaymentMethod())
                .orElse("No payment found");
        orderDetailDTO.setPaymentDetail(paymentDetail);

        // set up shipment details if there is a record
        String ShipmentDetail = shipmentRepository.findShipmentByOrder_Id(UUID.fromString(orderId))
                .map(shipment -> shipment.getTrackingNumber() + "#" + shipment.getStatus() + "#" + shipment.getShippedDate() + "#"
                + shipment.getAddress().toString())
                .orElse("No shipment found");
        orderDetailDTO.setShipmentDetail(ShipmentDetail);
        return Optional.of(orderDetailDTO);
    }

    public void updateOrder(Order order) {
        orderRepository.save(order);
    }

    public Page<OrderResponseDTO> fetchOrderByMerchantId(Long merchantId, int page, int size ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Order.desc("orderCreatedTime")));
        Page<Order> orderPage = orderRepository.findByMerchantId(merchantId,pageable);
        return orderPage.map(OrderResponseDTO::new);
    }

    public void clearOrders() {
        orderRepository.deleteAll();
    }
}
