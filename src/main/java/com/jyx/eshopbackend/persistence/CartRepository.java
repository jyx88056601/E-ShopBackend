package com.jyx.eshopbackend.persistence;

import com.jyx.eshopbackend.model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface CartRepository extends JpaRepository<Cart,Long> {

    Optional<Cart> findCartByUser_Id(Long userId);

}
