package com.example.shopapp.controller;

import com.example.shopapp.dto.ProductDTO;
import com.example.shopapp.dto.ResponseDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/products")
public class ProductController {
    @GetMapping("")
    public ResponseEntity<?> getAllProducts(@Valid @RequestBody ProductDTO productDTO, BindingResult bindingResult) {
//        Integer page = 1, limit;
//        if(params.containsKey("page")) {
//            page = Integer.parseInt(params.get("page").toString());
//        }
        ResponseDTO responseDTO = new ResponseDTO();

        if(bindingResult.hasErrors()) {
            responseDTO.setErrors(bindingResult.getFieldErrors().stream().map(FieldError::getDefaultMessage).collect(Collectors.toList()));
            return ResponseEntity.badRequest().body(responseDTO);
        }
        responseDTO.setData(productDTO);

        return ResponseEntity.ok(responseDTO);
    }

    @PostMapping
    public String createProduct(){
        return "Create a product";
    }

    @PutMapping("/{id}")
    public String updateProduct(@PathVariable("id") Long productId){
        return "Update product by id = " + productId;
    }

    @DeleteMapping("/{id}")
    public String deleteProduct(@PathVariable("id") Long productId){
        return "Delete product by id = " + productId;
    }
}
