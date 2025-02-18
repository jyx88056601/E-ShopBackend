package com.jyx.eshopbackend.presentation;

import com.jyx.eshopbackend.aws.S3Service;
import com.jyx.eshopbackend.dto.ProductUpLoadingResponseDTO;
import com.jyx.eshopbackend.dto.ProductUploadDTO;
import com.jyx.eshopbackend.service.ProductService;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/business")
public class BusinessController {
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(BusinessController.class);

    private final S3Service s3Service;

    private final ProductService productService;


    public BusinessController(S3Service s3Service, ProductService productService) {
        this.s3Service = s3Service;
        this.productService = productService;
    }

    @GetMapping("/findProductsByOwner{id}")
    public ResponseEntity<Object> displayAllProducts(@PathVariable String id,@RequestParam String page,@RequestParam String size) {
        logger.info("request from frontend to current port with BusinessController.displayAllProducts()");
        logger.info("request is findProductsByOwner" + id);
        try {
            productService.findProductsByOwnerId(id, page, size);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        return ResponseEntity.status(HttpStatus.OK).body(productService.findProductsByOwnerId(id,page, size));
    }

    @PostMapping("/uploadingBy{id}")
    public ResponseEntity<Object> uploadProduct(
            @PathVariable String id,
            @RequestParam("name") String name,
            @RequestParam("price") String price,
            @RequestParam("stock") String stock,
            @RequestParam("category") String category,
            @RequestParam("description") String description,
            @RequestParam("images") List<MultipartFile> images)  {
        List<String> urls = new ArrayList<>();
        for(MultipartFile image : images) {
            File file = new File(System.getProperty("java.io.tmpdir") + "/" + image.getOriginalFilename());
            try {
                image.transferTo(file);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            String fileUrl = s3Service.uploadFile(file);
            if (fileUrl != null) {
                urls.add(fileUrl);
            }
        }
        Optional<ProductUpLoadingResponseDTO> response;
        try {
            response = productService.saveProduct(new ProductUploadDTO(name,price,stock,category,description,urls, id));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
        if(response.isEmpty()) return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        return ResponseEntity.ok(Map.of("status",HttpStatus.OK,"ProductUpLoadingResponseDTO", response));
    }


    @DeleteMapping("/deleteBy{productId}")
    public ResponseEntity<String> deleteProductById(@PathVariable String productId) {
        String result;
       try {
           result = productService.removeProduct(Long.parseLong(productId));
       } catch (Exception e) {
           return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
       }
       return  ResponseEntity.status(HttpStatus.OK).body(result);
    }

}
