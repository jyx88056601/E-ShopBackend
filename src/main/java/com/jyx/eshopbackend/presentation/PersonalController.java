package com.jyx.eshopbackend.presentation;

import com.jyx.eshopbackend.dto.*;
import com.jyx.eshopbackend.model.Cart;
import com.jyx.eshopbackend.model.CartItem;
import com.jyx.eshopbackend.service.CartService;
import com.jyx.eshopbackend.service.OrderService;
import com.jyx.eshopbackend.service.ProductService;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
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

    private final OrderService orderService;

    private final PagedResourcesAssembler<OrderResponseDTO> pageAssembler;



    public PersonalController(ProductService productService, CartService cartService, OrderService orderService, PagedResourcesAssembler<OrderResponseDTO> pageAssembler) {
        this.productService = productService;
        this.cartService = cartService;
        this.orderService = orderService;
        this.pageAssembler = pageAssembler;
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
        logger.info("PersonalController.addToCart()");
        logger.info("POST:/cart/add-to-cart/user_id=" + user_id);
         for(var c : cartItemRequestDTOList) {
             System.out.println(c.getProduct_id() + " " + c.getQuantity());
         }
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
        logger.info("PersonalController.deleteCartItems()");
        logger.info("DELETE:/delete-cartItems");
        Set<Long> cartItemIds = new HashSet<>();
        for(String id : ids) {
            cartItemIds.add(Long.parseLong(id));
        }
         cartService.deleteCartItemsByIds(Long.parseLong(cartId),cartItemIds);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PostMapping("/create-order/user_id={customerId}")
    public ResponseEntity<Object> createAnOrder(@PathVariable String customerId, @RequestBody OrderRequestDTO orderRequestDTO) {
        logger.info("PersonalController.createAnOrder()");
        logger.info("Post:/create-order");
        // split order items based on merchant ids
        if(orderRequestDTO.getOrderItemRequestDTOList().isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        List<OrderResponseDTO> orderResponseDTOList = new ArrayList<>();
        Map<String, List<OrderItemRequestDTO>> orderRequestDTOMap = new HashMap<>();
        for(OrderItemRequestDTO orderItemRequestDTO : orderRequestDTO.getOrderItemRequestDTOList()) {
            String merchantId = orderItemRequestDTO.getMerchantId();
            if(orderRequestDTOMap.containsKey(merchantId)) {
                orderRequestDTOMap.get(merchantId).add(orderItemRequestDTO);
            } else {
               List<OrderItemRequestDTO> orderItemRequestDTOList = new ArrayList<>();
               orderItemRequestDTOList.add(orderItemRequestDTO);
               orderRequestDTOMap.put(merchantId,orderItemRequestDTOList);
            }
        }
        for(String merchantId : orderRequestDTOMap.keySet()) {
            System.out.println(merchantId);
        }
        try {
            for(String merchantId : orderRequestDTOMap.keySet()) {
                orderResponseDTOList.add(orderService.createOrder(customerId, merchantId, new OrderRequestDTO(orderRequestDTOMap.get(merchantId))).get());
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
        return ResponseEntity.status(HttpStatus.OK).body(orderResponseDTOList);
    }

    @GetMapping("/fetchOrders/user_id={customerId}")
    public ResponseEntity<Object> fetchOrders(@PathVariable String customerId, @RequestParam String page, @RequestParam String size) {
        logger.info("PersonalController.fetchOrders()");
        logger.info("/fetchOrders/user_id=" + customerId);
        Page<OrderResponseDTO> orders = orderService.fetchOrderByCustomerId(Long.parseLong(customerId),Integer.parseInt(page), Integer.parseInt(size));
        PagedModel<EntityModel<OrderResponseDTO>> pagedModel = pageAssembler.toModel(orders);
        return ResponseEntity.ok(pagedModel);
    }

    @DeleteMapping("/deleteOrder/order_id={orderId}")
    public ResponseEntity<Object> deleteOrder(@PathVariable String orderId) {
        orderService.removeOrder(UUID.fromString(orderId));
        return ResponseEntity.status(HttpStatus.OK).build();
    }

}
