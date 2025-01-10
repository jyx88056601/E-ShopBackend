package com.jyx.eshopbackend.persistence;

import com.jyx.eshopbackend.model.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrderItemRepository extends JpaRepository<OrderItem,Long> {
    Optional<List<OrderItem>> findOrderItemsByOrder_Id(Long orderId);
}
