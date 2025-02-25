package com.jyx.eshopbackend.service;

import com.amazonaws.services.kms.model.NotFoundException;
import com.jyx.eshopbackend.dto.CartItemRequestDTO;
import com.jyx.eshopbackend.dto.CartRequestDTO;
import com.jyx.eshopbackend.model.Cart;
import com.jyx.eshopbackend.model.CartItem;
import com.jyx.eshopbackend.persistence.CartRepository;
import com.jyx.eshopbackend.persistence.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class CartService {
    private final CartRepository cartRepository;

    private final ProductRepository productRepository;

    public CartService(CartRepository cartRepository, ProductRepository productRepository) {
        this.cartRepository = cartRepository;
        this.productRepository = productRepository;
    }

    public void initializeCart(Cart cart) {
        cartRepository.save(cart);
    }

    public Optional<Cart> findCartById(Long id) {
        return cartRepository.findCartByUser_Id(id);
    }

    @Transactional
    public Optional<Cart> addToCart(CartRequestDTO cartRequestDTO) {
        Long id = Long.parseLong(cartRequestDTO.getCartId());


        Cart cart = cartRepository.findCartByUser_Id(id)
                .orElseThrow(() -> new NotFoundException("No cart found"));


        Map<Long, CartItem> cartItemMap = new HashMap<>();
        for (CartItem cartItem : cart.getCartItems()) {
            cartItemMap.put(cartItem.getProduct().getId(), cartItem);
        }


        List<CartItemRequestDTO> cartItemRequestDTOList = cartRequestDTO.getProductRequestDTO();
        for (CartItemRequestDTO cartItemRequestDTO : cartItemRequestDTOList) {
            Long productId = Long.parseLong(cartItemRequestDTO.getProduct_id());
            int quantity = Integer.parseInt(cartItemRequestDTO.getQuantity());

            if (cartItemMap.containsKey(productId)) {

                cartItemMap.get(productId).setQuantity(quantity);
            } else {

                CartItem cartItem = new CartItem();
                cartItem.setProduct(productRepository.findById(productId)
                        .orElseThrow(() -> new RuntimeException("No product found")));
                cartItem.setQuantity(quantity);
                cartItem.setCart(cart);
                cartItemMap.put(productId, cartItem);
            }
        }


        cart.getCartItems().clear();
        cart.getCartItems().addAll(cartItemMap.values());


        return Optional.of(cartRepository.save(cart));
    }

    @Transactional
    public void deleteCartItemsByIds(Long cartId, Set<Long> cartItemIds) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new RuntimeException("Cart not found"));
        cart.getCartItems().removeIf(item -> cartItemIds.contains(item.getId()));
        cartRepository.save(cart);
    }
}
