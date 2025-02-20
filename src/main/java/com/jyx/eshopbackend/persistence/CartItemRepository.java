package com.jyx.eshopbackend.persistence;

import com.jyx.eshopbackend.model.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface CartItemRepository extends JpaRepository<CartItem,Long> {
    List<CartItem> findCartItemsByCart_id(Long cartId);

}
