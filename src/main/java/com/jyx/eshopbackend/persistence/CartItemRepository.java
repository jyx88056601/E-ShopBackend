package com.jyx.eshopbackend.persistence;

import com.jyx.eshopbackend.model.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CartItemRepository extends JpaRepository<CartItem,Long> {
    Optional<List<CartItem>> findCartItemsByCart_Id(Long cartId);
}
