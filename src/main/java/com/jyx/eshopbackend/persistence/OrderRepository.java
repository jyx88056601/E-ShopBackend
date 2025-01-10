package com.jyx.eshopbackend.persistence;

import com.jyx.eshopbackend.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order,Long> {

    Optional<List<Order>> findOrdersByUser_Id(Long userId);

}
