package com.example.shopapp.components.converter;

import com.example.shopapp.dtos.ProductDTO;
import com.example.shopapp.entities.Product;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ProductConverter {
    @Autowired
    private ModelMapper modelMapper;

    public Product convertToEntity(ProductDTO productDTO){
        return modelMapper.map(productDTO, Product.class);
    }

    public ProductDTO convertToDTO(Product product){
        if(product == null) return null;
        return modelMapper.map(product, ProductDTO.class);
    }
}
