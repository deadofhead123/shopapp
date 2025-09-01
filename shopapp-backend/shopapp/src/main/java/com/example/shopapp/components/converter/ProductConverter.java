package com.example.shopapp.components.converter;

import com.example.shopapp.entities.Product;
import com.example.shopapp.entities.ProductImage;
import com.example.shopapp.models.dtos.ProductDTO;
import com.example.shopapp.models.responses.ProductImageResponse;
import com.example.shopapp.models.responses.ProductResponse;
import com.example.shopapp.repositories.ProductImageRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ProductConverter {
    private final ModelMapper modelMapper;
    private final ProductImageRepository productImageRepository;
    private final ProductImageConverter productImageConverter;

    public Product convertToEntity(ProductDTO productDTO){
        return modelMapper.map(productDTO, Product.class);
    }

    public ProductDTO convertToDTO(Product product){
        if(product == null) return null;
        return modelMapper.map(product, ProductDTO.class);
    }

    public ProductResponse convertToResponse(Product product){
        if(product == null) return null;

        ProductResponse productResponse = modelMapper.map(product, ProductResponse.class);
        productResponse.setCategoryId(product.getCategory().getId());

        List<ProductImage> productImages = productImageRepository.findByProductId(product.getId());
        List<ProductImageResponse> productImageResponses = productImages.stream().map(productImageConverter::convertToResponse).toList();
        productResponse.setProductImageResponses(productImageResponses);

        return productResponse;
    }
}
