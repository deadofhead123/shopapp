package com.example.shopapp.services.product;

import com.example.shopapp.dtos.ProductDTO;
import com.example.shopapp.dtos.ProductImageDTO;
import com.example.shopapp.entities.Product;
import com.example.shopapp.entities.ProductImage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

public interface IProductService {
    Product createProduct(ProductDTO productDTO);

    ProductDTO getProductById(Long productId);
    Page<ProductDTO> getAllProducts(PageRequest request);

    ProductDTO updateProduct(Long productId, ProductDTO productDTO);
    Boolean deleteProduct(Long productId);

    ProductImage createProductImage(ProductImageDTO productImageDTO);
}
