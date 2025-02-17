package com.jyx.eshopbackend.service;

import com.jyx.eshopbackend.dto.ProductDetailDTO;
import com.jyx.eshopbackend.dto.ProductUpLoadingResponseDTO;
import com.jyx.eshopbackend.dto.ProductUploadDTO;
import com.jyx.eshopbackend.model.Product;
import com.jyx.eshopbackend.model.ProductImage;
import com.jyx.eshopbackend.persistence.ProductImageRepository;
import com.jyx.eshopbackend.persistence.ProductRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final ProductImageRepository productImageRepository;

    public ProductService(ProductRepository productRepository, ProductImageRepository productImageRepository) {
        this.productRepository = productRepository;
        this.productImageRepository = productImageRepository;
    }

    public Page<ProductDetailDTO> findProductsByOwnerId(String ownerId, String page, String size) {
        int pageNumber = Integer.parseInt(page);
        int pageSize =  Integer.parseInt(size);
        if (pageNumber < 0 || pageSize <= 0) {
            throw new IllegalArgumentException("Page number cannot be negative and size must be greater than 0.");
        }
        Pageable pageable = PageRequest.of(pageNumber,pageSize);
        Page<Product> products = productRepository.findByOwnerId(Long.parseLong(ownerId), pageable);
        return products.map(ProductDetailDTO::new);
    }

    @Transactional
    public Optional<ProductUpLoadingResponseDTO> saveProduct(ProductUploadDTO productUploadDTO) {
        Product product = new Product();
        product.setName(productUploadDTO.getName());
        product.setCategory(productUploadDTO.getCategory());
        product.setDescription(productUploadDTO.getDescription());
        product.setStock(Integer.parseInt(productUploadDTO.getStock()));
        product.setPrice(new BigDecimal(productUploadDTO.getPrice()));
        product.setOwnerId(Long.parseLong(productUploadDTO.getId()));
        // store images urls to mysql
        List<ProductImage> productImages = new ArrayList<>();
        List<String> urls = productUploadDTO.getImages();
        for (String url : urls) {
            ProductImage productImage = new ProductImage();
            productImage.setUrl(url);
            productImage.setProduct(product);
            productImage.setMain(urls.indexOf(url) == 0);
            ProductImage savedImage = saveProductImage(productImage);
            if (savedImage == null) {
                throw new RuntimeException("Can't save image with URL: " + url);
            }
            productImages.add(savedImage);
        }
        product.setProductImages(productImages);
        Product savedProduct = productRepository.save(product);
        return Optional.of(new ProductUpLoadingResponseDTO(savedProduct));
    }

    public ProductImage saveProductImage(ProductImage productImage) {
        return productImageRepository.save(productImage);
    }

}

/*
public Optional<String> removeUserByUsername(String username) throws Exception {
        Optional<User> user  = userRepository.findByUsername(username);
        if(user.isEmpty()) throw new UsernameNotFoundException("User does not exist");
        userRepository.deleteById(user.get().getId());
        if (userRepository.existsById(user.get().getId())) {
            throw new UserDeletionFailedException("Deleting " +  username +" failed");
        }
        return Optional.of("User " + username + " has been deleted");
    }
    */
