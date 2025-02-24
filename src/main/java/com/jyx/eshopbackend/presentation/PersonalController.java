package com.jyx.eshopbackend.presentation;

import com.jyx.eshopbackend.dto.*;
import com.jyx.eshopbackend.model.Cart;
import com.jyx.eshopbackend.model.CartItem;
import com.jyx.eshopbackend.service.CartService;
import com.jyx.eshopbackend.service.ProductService;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/personal")
public class PersonalController {

    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(BusinessController.class);

    private final ProductService productService;

    private final CartService cartService;

    public PersonalController(ProductService productService, CartService cartService) {
        this.productService = productService;
        this.cartService = cartService;
    }

    @GetMapping("/products")
    public ResponseEntity<Object> displayProducts (@RequestParam String page, @RequestParam String size){
        logger.info("PersonalController.displayProducts()");
        logger.info("GET:/personal/products");
        Page<ProductDetailDTO> productPage;
        try {
            productPage = productService.findProducts(page, size);
        } catch (Exception e) {
            logger.warn("Catch Exception while fetching data from database");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        return ResponseEntity.status(HttpStatus.OK).body(productPage);
    }

    @GetMapping("/product/{id}")
    public ResponseEntity<Object> findProductById (@PathVariable String id){
        logger.info("PersonalController.findProductById()");
        logger.info("GET:/personal/product/" + id);
        Optional<ProductDetailDTO> productDetailDTO;
        try {
            productDetailDTO = productService.findProductDetailById(Long.parseLong(id));
        } catch (Exception e) {
            logger.warn("Catch Exception while fetching data from database");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        return ResponseEntity.status(HttpStatus.OK).body(productDetailDTO.get());
    }

    @GetMapping("/cart/user_id={user_id}")
    public ResponseEntity<Object> findCartById(@PathVariable String user_id) {
        logger.info("PersonalController.findCartById()");
        logger.info("GET:/personal/cart/" + user_id);
        var cart = cartService.findCartById(Long.parseLong(user_id)).orElseThrow(() -> new RuntimeException("No cart found with" + user_id));
        List<CartItemResponseDTO> cartItemRequestDTOList = new ArrayList<>();
        for (CartItem cartItem : cart.getCartItems()) {
            ProductSimplifiedResponseDTO productSimplifiedResponseDTO = new ProductSimplifiedResponseDTO(cartItem.getProduct());
            cartItemRequestDTOList.add(new CartItemResponseDTO(String.valueOf(cartItem.getId()), String.valueOf(cartItem.getQuantity()),productSimplifiedResponseDTO));
        }
        CartResponseDTO cartResponseDTO = new CartResponseDTO(user_id, cartItemRequestDTOList);
        return ResponseEntity.status(HttpStatus.OK).body(cartResponseDTO);
    }

    @PostMapping("/cart/add-to-cart/user_id={user_id}")
    public ResponseEntity<Object> addToCart(@PathVariable String user_id, @RequestBody List<CartItemRequestDTO> cartItemRequestDTOList) {
        Optional<Cart> cart;
        try {
            cart =  cartService.addToCart(new CartRequestDTO(user_id, cartItemRequestDTOList));
        } catch (Exception e) {
            logger.warn(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        return  ResponseEntity.status(HttpStatus.OK).body(cart.get());
    }

    @DeleteMapping("/delete-cartItems/{cartId}")
    public ResponseEntity<Object> deleteCartItems(@PathVariable String cartId, @RequestBody List<String> ids) {
        logger.info("PersonalController.deleteCartItems");
        logger.info("DELETE:/delete-cartItems");
        Set<Long> cartItemIds = new HashSet<>();
        for(String id : ids) {
            cartItemIds.add(Long.parseLong(id));
        }
         cartService.deleteCartItemsByIds(Long.parseLong(cartId),cartItemIds);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

}
