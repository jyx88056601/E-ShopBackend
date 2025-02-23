package com.jyx.eshopbackend.service;

import com.amazonaws.services.kms.model.NotFoundException;
import com.jyx.eshopbackend.dto.CartItemRequestDTO;
import com.jyx.eshopbackend.dto.CartRequestDTO;
import com.jyx.eshopbackend.model.Cart;
import com.jyx.eshopbackend.model.CartItem;
import com.jyx.eshopbackend.persistence.CartItemRepository;
import com.jyx.eshopbackend.persistence.CartRepository;
import com.jyx.eshopbackend.persistence.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class CartService {

    private final CartRepository cartRepository;

    private final CartItemRepository cartItemRepository;

    private final ProductRepository productRepository;

    public CartService(CartRepository cartRepository, CartItemRepository cartItemRepository, ProductRepository productRepository) {
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
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

        for (CartItemRequestDTO cartItemRequestDTO : cartRequestDTO.getProductRequestDTO()) {
            Long productId;
            try {
                productId = Long.parseLong(cartItemRequestDTO.getProduct_id());
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Invalid product ID: " + cartItemRequestDTO.getProduct_id());
            }

            CartItem cartItem = cartItemRepository.findCartItemsByProduct_id(productId);

            if (cartItem != null) {
                cartItem.setQuantity(Integer.parseInt(cartItemRequestDTO.getQuantity()));
            } else {
                cartItem = new CartItem();
                cartItem.setCart(cart);
                cartItem.setProduct(productRepository.findProductById(productId)
                        .orElseThrow(() -> new NotFoundException("No product with id " + productId   + "can be found")));
                cartItem.setQuantity(Integer.parseInt(cartItemRequestDTO.getQuantity()));
            }

            cart.getCartItems().add(cartItem);
        }

        return Optional.of(cartRepository.save(cart));
    }

    public Optional<CartItem> addCartItem(CartItem cartItem) {
        return Optional.of(cartItemRepository.save(cartItem));
    }


}
