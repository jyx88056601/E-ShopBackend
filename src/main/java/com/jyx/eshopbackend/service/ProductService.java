package com.jyx.eshopbackend.service;

import com.jyx.eshopbackend.aws.S3Service;
import com.jyx.eshopbackend.dto.ProductDetailDTO;
import com.jyx.eshopbackend.dto.ProductUpLoadingResponseDTO;
import com.jyx.eshopbackend.dto.ProductUploadDTO;
import com.jyx.eshopbackend.model.Product;
import com.jyx.eshopbackend.model.ProductImage;
import com.jyx.eshopbackend.model.User;
import com.jyx.eshopbackend.persistence.CartItemRepository;
import com.jyx.eshopbackend.persistence.ProductImageRepository;
import com.jyx.eshopbackend.persistence.ProductRepository;
import com.jyx.eshopbackend.persistence.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;

@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final ProductImageRepository productImageRepository;
    private final UserService userService;
    private final S3Service s3Service;
    private final UserRepository userRepository;
    private final CartItemRepository cartItemRepository;


    // Constructor to inject dependencies
    public ProductService(ProductRepository productRepository, ProductImageRepository productImageRepository,
                          UserService userService, S3Service s3Service,
                          UserRepository userRepository,
                          CartItemRepository cartItemRepository) {
        this.productRepository = productRepository;
        this.productImageRepository = productImageRepository;
        this.userService = userService;
        this.s3Service = s3Service;
        this.userRepository = userRepository;
        this.cartItemRepository = cartItemRepository;
    }

    // Find products by ownerId
    @Transactional
    public Page<ProductDetailDTO> findProductsByOwnerId(String ownerId, String page, String size) {
        int pageNumber = Integer.parseInt(page);
        int pageSize = Integer.parseInt(size);

        if (pageNumber < 0 || pageSize <= 0) {
            throw new IllegalArgumentException("Page number cannot be negative and size must be greater than 0.");
        }

        User user = userService.findUserById(Long.parseLong(ownerId))
                .orElseThrow(() -> new UsernameNotFoundException("Invalid user id"));

        Set<Long> productIds = user.getProductIds();
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<Product> products = productRepository.findByIdIn(new ArrayList<>(productIds), pageable);
        return products.map(ProductDetailDTO::new);
    }

    // Save new product and images
    @Transactional
    public Optional<ProductUpLoadingResponseDTO> saveProduct(ProductUploadDTO productUploadDTO) {
        Product product = new Product();
        product.setName(productUploadDTO.getName());
        product.setCategory(productUploadDTO.getCategory());
        product.setDescription(productUploadDTO.getDescription());
        product.setStock(Integer.parseInt(productUploadDTO.getStock()));
        product.setPrice(new BigDecimal(productUploadDTO.getPrice()));
        product.setOwnerId(Long.parseLong(productUploadDTO.getId()));

        // Store images URLs in MySQL
        List<ProductImage> productImages = new ArrayList<>();
        List<String> urls = productUploadDTO.getImages();
        for (String url : urls) {
            ProductImage productImage = new ProductImage();
            productImage.setUrl(url);
            productImage.setProduct(product);
            if(urls.indexOf(url) == 0) {
                productImage.setMain(true);
                product.setMainPictureUrl(url);
            }
            productImage.setMain(urls.indexOf(url) == 0);
            ProductImage savedImage = saveProductImage(productImage);
            if (savedImage == null) {
                throw new RuntimeException("Can't save image with URL: " + url);
            }
            productImages.add(savedImage);
        }
        product.setProductImages(productImages);

        // Save the product in the database
        Product savedProduct = productRepository.save(product);

        // Add product ID to the user
        User user = userService.findUserById(savedProduct.getOwnerId()).get();
        user.getProductIds().add(savedProduct.getId());
        userService.updateUser(user);

        return Optional.of(new ProductUpLoadingResponseDTO(savedProduct));
    }

    // Save a product image
    public ProductImage saveProductImage(ProductImage productImage) {
        return productImageRepository.save(productImage);
    }

    // Remove product and its images from database and AWS
   @Transactional
    public String removeProduct(Long productId) {

        // Find product from the database
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new NoSuchElementException("Product not found"));

        // remove from business account product list
        Long ownerId = product.getOwnerId();
         var user = userRepository.findById(ownerId).orElseThrow(() -> new RuntimeException("no user found"));
         user.getProductIds().remove(productId);
         userService.updateUser(user);

       // Delete product images from AWS
       StringBuilder sb = new StringBuilder();
       for (ProductImage productImage : product.getProductImages()) {
           String url = productImage.getUrl();
           String fileName = url.split("amazonaws.com/")[1];
           sb.append(s3Service.removeFile(fileName)); // Remove file from S3
       }



       // Delete product from MySQL database
       productRepository.deleteById(productId);

        return "Product with ID " + productId + " has been deleted. AWS server alert: " + sb;
    }

    // fetch products to be shown to buyers
    public Page<ProductDetailDTO> findProducts(String page, String size) {
        int pageNumber = Integer.parseInt(page);
        int pageSize = Integer.parseInt(size);
        if (pageNumber < 0 || pageSize <= 0) {
            throw new IllegalArgumentException("Page number cannot be negative and size must be greater than 0.");
        }
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<Product> products = productRepository.findAll(pageable);
        return products.map(ProductDetailDTO::new);
    }

    // find the product detail by id
    public Optional<ProductDetailDTO> findProductDetailById(Long id) {
         return productRepository.findById(id).map(ProductDetailDTO::new);
    }


    // dynamic query by Criteria API
/*    public void fetchProductsByCategory() {
          var products = productRepository.findProductsByCriteria(null, BigDecimal.ZERO, BigDecimal.valueOf(100000));
          products.forEach((product) -> System.out.println(product.getName()));
      } */


//    public void fetchProductsBySpecification(String name, BigDecimal minPrice, BigDecimal maxPrice) {
//        Specification<Product> spec = Specification.where(null);
//        if(name != null) {
//            spec = spec.and(ProductSpec.hasName(name));
//        }
//
//        if(minPrice != null) {
//            spec = spec.and(ProductSpec.hasPriceGreaterThanOrEqualTo(minPrice));
//        }
//
//        if(maxPrice != null) {
//            spec = spec.and(ProductSpec.hasPriceLessThanOrEqualTo(maxPrice));
//        }
//
//         productRepository.findAll(spec).forEach(product -> {
//             System.out.println(product.getName());
//         });
//    }


}
