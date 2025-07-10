package com.example.shopapp.controllers;

import com.example.shopapp.constant.SystemConstant;
import com.example.shopapp.dtos.ProductDTO;
import com.example.shopapp.dtos.ProductImageDTO;
import com.example.shopapp.dtos.ResponseDTO;
import com.example.shopapp.entities.Product;
import com.example.shopapp.entities.ProductImage;
import com.example.shopapp.services.product.IProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/products")
public class ProductController {
    private final IProductService productService;
    private final String productImageDirectoryPrefix = "\\products";

    @GetMapping("")
    public ResponseEntity<?> getAllProducts(
            @RequestParam(name = "limit") Integer limit,
            @RequestParam(name = "page") Integer page) {
        ResponseDTO responseDTO = new ResponseDTO();

        try{
            responseDTO.setData(productService.getAllProducts(PageRequest.of(page, limit)));

            return ResponseEntity.ok(responseDTO);
        }
        catch(Exception e){
            responseDTO.setMessage(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseDTO);
        }
    }

    @PostMapping(value = "", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> createProduct(@Valid @ModelAttribute ProductDTO productDTO, BindingResult bindingResult) {
        ResponseDTO responseDTO = new ResponseDTO();

        try{
            if(bindingResult.hasErrors()) {
                responseDTO.setErrors(
                        bindingResult.getFieldErrors().
                                stream().
                                map(FieldError::getDefaultMessage).
                                collect(Collectors.toList()));
                return ResponseEntity.badRequest().body(responseDTO);
            }

            Product newProduct = productService.createProduct(productDTO);
            responseDTO.setData(newProduct);

            List<MultipartFile> files = productDTO.getFiles();
            files = files == null ? new ArrayList<>() : files;

            for(MultipartFile file : files){
                if(file.getSize() == 0){
                    continue;
                }

                if(file.getSize() > SystemConstant.maxImageFileSize){
                    responseDTO.setMessage("File is too large");
                    return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE).body(responseDTO);
                }

                String contentType = file.getContentType();
                if(contentType == null || !contentType.startsWith("image/")){
                    responseDTO.setMessage("This isn't an image");
                    return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE).body(responseDTO);
                }

                String thumbnailSingle = saveImage(file);
                ProductImage productImage = productService.createProductImage(ProductImageDTO.builder()
                        .imageUrl(thumbnailSingle)
                        .productId(newProduct.getId()).build());
            }

            return ResponseEntity.ok(responseDTO);
        }
        catch(Exception e){
            responseDTO.setMessage(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseDTO);
        }
    }

    public String saveImage(MultipartFile file) throws IOException {
        // Folder to save
        Path uploadDir = Paths.get(SystemConstant.imagePathPrefix + productImageDirectoryPrefix);
        if(Files.notExists(uploadDir)){
            Files.createDirectories(uploadDir);
        }

        // Image's name to save
        String imageName = StringUtils.cleanPath(file.getOriginalFilename());
        String uniqueFileName = UUID.randomUUID() + "_" + imageName;

        // Save
        Path destination = Paths.get(uploadDir.toString(), uniqueFileName);
        Files.copy(file.getInputStream(), destination, StandardCopyOption.REPLACE_EXISTING);

        return uniqueFileName;
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateProduct(@PathVariable("id") Long productId, @Valid @RequestBody ProductDTO productDTO, BindingResult bindingResult) {
        ResponseDTO responseDTO = new ResponseDTO();

        try{
            if(bindingResult.hasErrors()) {
                responseDTO.setErrors(
                        bindingResult.getFieldErrors().
                                stream().
                                map(FieldError::getDefaultMessage).
                                collect(Collectors.toList()));
                return ResponseEntity.badRequest().body(responseDTO);
            }

            responseDTO.setData(productService.updateProduct(productId, productDTO));

            return ResponseEntity.ok(responseDTO);
        }
        catch(Exception e){
            responseDTO.setMessage(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseDTO);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable("id") Long productId) {
        ResponseDTO responseDTO = new ResponseDTO();

        try{
            responseDTO.setData(productService.deleteProduct(productId));

            return ResponseEntity.ok(responseDTO);
        }
        catch(Exception e){
            responseDTO.setMessage(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseDTO);
        }
    }
}
