package com.jyx.eshopbackend.presentation;

import com.jyx.eshopbackend.dto.ProductDetailDTO;
import com.jyx.eshopbackend.service.ProductService;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/personal")
public class PersonalController {

    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(BusinessController.class);

    private final ProductService productService;

    public PersonalController(ProductService productService) {
        this.productService = productService;
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
}
