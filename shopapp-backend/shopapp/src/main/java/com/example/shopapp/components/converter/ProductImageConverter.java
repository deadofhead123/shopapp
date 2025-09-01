package com.example.shopapp.components.converter;

import com.example.shopapp.entities.ProductImage;
import com.example.shopapp.models.responses.ProductImageResponse;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProductImageConverter {
    private final ModelMapper modelMapper;

    public ProductImageResponse convertToResponse(ProductImage productImage){
        if(productImage.getId() == null) return null;
        return modelMapper.map(productImage, ProductImageResponse.class);
    }
}
