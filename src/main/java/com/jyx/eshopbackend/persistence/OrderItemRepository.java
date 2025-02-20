package com.jyx.eshopbackend.persistence;

import com.jyx.eshopbackend.model.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem,Long> {
    Optional<List<OrderItem>> findOrderItemsByOrder_Id(Long orderId);
    Optional<List<OrderItem>> findByOrder_Id(Long orderId);
}
