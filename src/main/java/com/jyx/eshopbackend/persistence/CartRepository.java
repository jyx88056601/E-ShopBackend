package com.jyx.eshopbackend.persistence;

import com.jyx.eshopbackend.model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart,Long> {

    Optional<Cart> findCartByUser_Id(Long userId);

}
