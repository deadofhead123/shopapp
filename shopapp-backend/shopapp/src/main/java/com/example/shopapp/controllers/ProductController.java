package com.example.shopapp.controllers;

import com.example.shopapp.constant.MessageKeys;
import com.example.shopapp.constant.SystemConstant;
import com.example.shopapp.entities.Product;
import com.example.shopapp.entities.ProductImage;
import com.example.shopapp.exceptions.InvalidParamException;
import com.example.shopapp.models.dtos.ProductDTO;
import com.example.shopapp.models.dtos.ProductImageDTO;
import com.example.shopapp.models.dtos.ResponseDTO;
import com.example.shopapp.models.responses.ProductListResponse;
import com.example.shopapp.models.responses.ProductResponse;
import com.example.shopapp.services.product.IProductService;
import com.example.shopapp.utils.LocalizationUtil;
import com.github.javafaker.Faker;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
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
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/products")
public class ProductController {
    private final IProductService productService;
    private final String productImageDirectoryPrefix = "\\products\\";
    private final LocalizationUtil localizationUtil;

    @GetMapping("")
    public ResponseEntity<?> getAllProducts(
            @RequestParam(name = "limit") Integer limit,
            @RequestParam(name = "page") Integer page,
            @RequestParam(name = "keyword", required = false) String keyword,
            @RequestParam(name = "categoryId", required = false) Integer categoryId) {
        ResponseDTO responseDTO = new ResponseDTO();

        try{
//            Sort sort = Sort.by(Sort.Direction.valueOf(direction), sortField);
            Sort sort = Sort.by(Sort.Direction.ASC, "id");
            Page<ProductResponse> productResponseList = productService.getAllProducts(keyword, categoryId, PageRequest.of(page - 1, limit, sort));

            int totalPages = productResponseList.getTotalPages();
            List<ProductResponse> products = productResponseList.getContent();
            
            return ResponseEntity.ok(
                    ProductListResponse.builder()
                    .totalPages(totalPages)
                    .products(products)
                    .build()
            );
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

    @PostMapping(value = "/uploads/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadImages(
            @PathVariable("id") Long productId,
            @ModelAttribute("files") List<MultipartFile> files
    ){
        ResponseDTO responseDTO = new ResponseDTO();

        try{
            Product existingProduct = productService.getProductById(productId);
            List<ProductImage> productImages = new ArrayList<>();
            files = files == null ? new ArrayList<>() : files;

            if(files.size() > 5){
                responseDTO.setMessage(localizationUtil.getLocalizedMessage(MessageKeys.UPLOAD_IMAGES_MAX));
                return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE).body(responseDTO);
            }

            for(MultipartFile file : files){
                long fileSize = file.getSize();

                // Checking product image's number before adding a new image.
                int productImageNumber = productService.countProductImages(productId);
                if(productImageNumber >= ProductImage.MAX_IMAGE_PER_PRODUCT){
                    throw new InvalidParamException(localizationUtil.getLocalizedMessage(MessageKeys.UPLOAD_IMAGES_MAX));
                }

                // Checking media type, capacity of file
                if(fileSize == 0){
                    continue;
                }

                if(fileSize > SystemConstant.maxImageFileSize ){
                    responseDTO.setMessage(localizationUtil.getLocalizedMessage(MessageKeys.UPLOAD_IMAGES_FILE_LARGE));
                    return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE).body(responseDTO);
                }

                if(!Objects.requireNonNull(file.getContentType()).startsWith("image/")){
                    responseDTO.setMessage(localizationUtil.getLocalizedMessage(MessageKeys.UPLOAD_IMAGES_FILE_MUST_BE_IMAGE));
                    return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE).body(responseDTO);
                }

                String productImageName = saveImage(file);
                productImages.add(
                        productService.createProductImage(
                                ProductImageDTO.builder()
                                        .productId(productId)
                                        .imageUrl(productImageName)
                                        .build()
                        )
                );
            }

            responseDTO.setData(productImages);
            responseDTO.setMessage(localizationUtil.getLocalizedMessage(MessageKeys.UPLOAD_IMAGES_SUCCESSFULLY));
            return ResponseEntity.ok(responseDTO);
        }
        catch(Exception e){
            responseDTO.setMessage(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseDTO);
        }
    }

    private String saveImage(MultipartFile file) throws IOException {
        if(file.getOriginalFilename() == null){
            throw new IOException(localizationUtil.getLocalizedMessage(MessageKeys.UPLOAD_IMAGES_FILE_MUST_BE_IMAGE));
        }

        Path uploadDir = Path.of(SystemConstant.imagePathPrefix + productImageDirectoryPrefix);
        if(!Files.exists(uploadDir)){
            Files.createDirectories(uploadDir);
        }

        String imageName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
        String imageNameEncoded = UUID.randomUUID() + "_" + imageName;

        Files.copy(file.getInputStream(), Path.of(uploadDir.toString(), imageNameEncoded), StandardCopyOption.REPLACE_EXISTING);

        return imageNameEncoded;
    }

    @GetMapping("/images/{imageName}")
    public ResponseEntity<?> viewImage(@PathVariable("imageName") String imageName){
        try{
            Path imagePath = Paths.get(SystemConstant.imagePathPrefix + productImageDirectoryPrefix + imageName);
            UrlResource resource = new UrlResource(imagePath.toUri());

            if(resource.exists()){
                return ResponseEntity.ok()
                        .contentType(MediaType.IMAGE_JPEG)
                        .body(resource);
            }
            else{
                return ResponseEntity.notFound().build();
            }
        }
        catch(Exception ex){
            return ResponseEntity.notFound().build();
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

    // A tool used to fake data of an object: javafaker
//    @PostMapping("/fakeProductsGenerating")
    private ResponseEntity<?> fakeProductsGenerating() {
        ResponseDTO responseDTO = new ResponseDTO();

        try{
            Faker faker = new Faker();

            for(int i = 1 ; i <= 200 ; i++){
                String productName = faker.commerce().productName();
                if(productService.existsByName(productName)){
                    continue;
                }
                ProductDTO newProduct = ProductDTO
                        .builder()
                        .name(productName)
                        .price((float)faker.number().randomDouble(4, 1, 90_000_000))
                        .thumbnail("")
                        .description(faker.lorem().sentence())
                        .categoryId(faker.number().numberBetween(3L, 10L))
                        .build();
                productService.createProduct(newProduct);
            }
            responseDTO.setMessage("Generate all product successfully");
            return ResponseEntity.ok(responseDTO);
        }
        catch (Exception e){
            responseDTO.setMessage(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseDTO);
        }
    }
}
