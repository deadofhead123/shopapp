package com.example.shopapp.services.product;

import com.example.shopapp.components.converter.ProductConverter;
import com.example.shopapp.dtos.ProductDTO;
import com.example.shopapp.dtos.ProductImageDTO;
import com.example.shopapp.entities.Category;
import com.example.shopapp.entities.Product;
import com.example.shopapp.entities.ProductImage;
import com.example.shopapp.exceptions.DataNotFoundException;
import com.example.shopapp.exceptions.InvalidParamException;
import com.example.shopapp.repositories.CategoryRepository;
import com.example.shopapp.repositories.ProductImageRepository;
import com.example.shopapp.repositories.ProductRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class ProductService implements IProductService{
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ProductConverter productConverter;
    private final ProductImageRepository productImageRepository;

    @Override
    public Product createProduct(ProductDTO productDTO) {
        Category existingCategory = categoryRepository.findById(productDTO.getCategoryId())
                                                      .orElseThrow(() -> new DataNotFoundException("Category not found"));

        Product newProduct = productConverter.convertToEntity(productDTO);
        newProduct.setCategory(existingCategory);
        newProduct.setId(null);

        return productRepository.save(newProduct);
    }

    @Override
    public Product getProductById(Long productId) {
        return productRepository.findById(productId).get();
    }

    @Override
    public Page<ProductDTO> getAllProducts(PageRequest request) {
        Page<Product> products = productRepository.findAll(request);
        return products.map(productConverter::convertToDTO);
    }

    @Override
    public ProductDTO updateProduct(Long productId, ProductDTO productDTO) {
        Product existingProduct = productRepository.findById(productId).orElseThrow(() -> new DataNotFoundException("Product not found"));

        Category existingCategory = categoryRepository.findById(productDTO.getCategoryId())
                .orElseThrow(() -> new DataNotFoundException("Category not found"));

        existingProduct.setName(productDTO.getName());
        existingProduct.setPrice(productDTO.getPrice());
        existingProduct.setThumbnail(productDTO.getThumbnail());
        existingProduct.setDescription(productDTO.getDescription());
        existingProduct.setCategory(existingCategory);

        return productConverter.convertToDTO(productRepository.save(existingProduct));
    }

    @Override
    public Boolean deleteProduct(Long productId) {
        Product existingProduct = productRepository.findById(productId).orElseThrow(() -> new DataNotFoundException("Product not found"));
        productRepository.delete(existingProduct);
        return true;
    }

    @Override
    public ProductImage createProductImage(ProductImageDTO productImageDTO) {
        Product product = productRepository.findById(productImageDTO.getProductId()).orElseThrow(() -> new DataNotFoundException("Product not found"));

        ProductImage newProductImage = ProductImage.builder()
                .imageUrl(productImageDTO.getImageUrl())
                .product(product)
                .build();

        // Cannot insert more than 5 image of a product (Check in database)
        /* Problem: a product have 4 image exists in "product_images" table. Your purpose is to add 2 images.
        After add 1 images -> 5 images -> maximum -> Cannot add 1 remaining image -> Should solve this?
        */
        int maxImageNumber = productImageRepository.findByProductId(productImageDTO.getProductId()).size();
        if(maxImageNumber >= 5){
            throw new InvalidParamException("Number of images cannot > 5");
        }

        return productImageRepository.save(newProductImage);
    }
}
