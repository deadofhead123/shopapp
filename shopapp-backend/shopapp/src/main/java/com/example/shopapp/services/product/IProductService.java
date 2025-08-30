package com.example.shopapp.services.product;

import com.example.shopapp.models.dtos.ProductDTO;
import com.example.shopapp.models.dtos.ProductImageDTO;
import com.example.shopapp.entities.Product;
import com.example.shopapp.entities.ProductImage;
import com.example.shopapp.models.responses.ProductResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

public interface IProductService {
    Product createProduct(ProductDTO productDTO);
    Product getProductById(Long productId);
    Page<ProductResponse> getAllProducts(String keyword, Integer categoryId, PageRequest request);
    ProductDTO updateProduct(Long productId, ProductDTO productDTO);
    Boolean deleteProduct(Long productId);
    ProductImage createProductImage(ProductImageDTO productImageDTO);
    boolean existsByName(String name);
    int countProductImages(Long productId);
}
