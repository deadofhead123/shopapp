package com.example.shopapp.controllers;

import com.example.shopapp.constant.SystemConstant;
import com.example.shopapp.models.dtos.ProductDTO;
import com.example.shopapp.models.dtos.ProductImageDTO;
import com.example.shopapp.models.dtos.ResponseDTO;
import com.example.shopapp.entities.Product;
import com.example.shopapp.entities.ProductImage;
import com.example.shopapp.exceptions.InvalidParamException;
import com.example.shopapp.services.product.IProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
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
            @RequestParam(name = "limit", defaultValue = "6") Integer limit,
            @RequestParam(name = "page", defaultValue = "0") Integer page,
            @RequestParam(name = "sort", defaultValue = "name") String sortField,
            @RequestParam(name = "direction", defaultValue = "asc") String direction) {
        ResponseDTO responseDTO = new ResponseDTO();

        try{
            Sort sort = Sort.by(Sort.Direction.valueOf(direction), sortField);
            responseDTO.setData(productService.getAllProducts(PageRequest.of(page, limit, sort)));

            return ResponseEntity.ok(responseDTO);
        }
        catch(Exception e){
            responseDTO.setMessage(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseDTO);
        }
    }

    @PostMapping(value = "")
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
            responseDTO.setData(productService.createProduct(productDTO));
            return ResponseEntity.ok(responseDTO);
        }
        catch(Exception e){
            responseDTO.setMessage(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseDTO);
        }
    }

    @PostMapping(value = "/uploads/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadImage(@PathVariable("id") Long productId, @ModelAttribute("files") List<MultipartFile> files){
        ResponseDTO responseDTO = new ResponseDTO();

        try{
            Product existingProduct = productService.getProductById(productId);

            // Prevent upload more than MAX_IMAGE_PER_PRODUCT images (From request) -> reduce check time in server side
            if(files.size() > ProductImage.MAX_IMAGE_PER_PRODUCT){
                throw new InvalidParamException("You can only upload up to " + ProductImage.MAX_IMAGE_PER_PRODUCT + " images");
            }

            List<ProductImage> productImages = new ArrayList<>();
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
                        .productId(existingProduct.getId()).build());
                productImages.add(productImage);
            }

            responseDTO.setData(productImages);
            return ResponseEntity.ok(responseDTO);
        }
        catch(Exception e){
            responseDTO.setMessage(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseDTO);
        }
    }

    public String saveImage(MultipartFile file) throws Exception {
        if(file.getOriginalFilename() == null){
            throw new IOException("Invalid format image");
        }

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
