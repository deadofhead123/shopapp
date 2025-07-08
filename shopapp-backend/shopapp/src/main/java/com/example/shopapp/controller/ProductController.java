package com.example.shopapp.controller;

import com.example.shopapp.constant.SystemConstant;
import com.example.shopapp.dto.ProductDTO;
import com.example.shopapp.dto.ResponseDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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
@RequestMapping("api/v1/products")
public class ProductController {
    @GetMapping("")
    public ResponseEntity<?> getAllProducts(@Valid @RequestBody ProductDTO productDTO, BindingResult bindingResult) {
//        Integer page = 1, limit;
//        if(params.containsKey("page")) {
//            page = Integer.parseInt(params.get("page").toString());
//        }
        ResponseDTO responseDTO = new ResponseDTO();

        if (bindingResult.hasErrors()) {
            responseDTO.setErrors(bindingResult.getFieldErrors().stream().map(FieldError::getDefaultMessage).collect(Collectors.toList()));
            return ResponseEntity.badRequest().body(responseDTO);
        }
        responseDTO.setData(productDTO);

        return ResponseEntity.ok(responseDTO);
    }

//    @PostMapping(value = "", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
//    //POST http://localhost:8088/v1/api/products
//    public ResponseEntity<?> createProduct(@Valid @ModelAttribute ProductDTO productDTO, BindingResult result) {
//        try {
//            if (result.hasErrors()) {
//                List<String> errorMessages = result.getFieldErrors()
//                        .stream()
//                        .map(FieldError::getDefaultMessage)
//                        .toList();
//                return ResponseEntity.badRequest().body(errorMessages);
//            }
//
//            List<MultipartFile> files = productDTO.getFiles();
//            files = files == null ? new ArrayList<MultipartFile>() : files;
//            for (MultipartFile file : files) {
//                if (file.getSize() == 0) {
//                    continue;
//                }
//                // Kiểm tra kích thước file và định dạng
//                if (file.getSize() > 10 * 1024 * 1024) { // Kích thước > 10MB
//                    return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE)
//                            .body("File is too large! Maximum size is 10MB");
//                }
//                String contentType = file.getContentType(); // khi dùng ajax để gửi dữ liệu tới server, có mục contentType ta thường hay để là "application/json; charset=UTF-8" -> Tóm lại chỉ kiểu dữ liệu
//                if (contentType == null || !contentType.startsWith("image/")) {
//                    return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
//                            .body("File must be an image");
//                }
//                // Lưu file và cập nhật thumbnail trong DTO
//                String filename = storeFile(file); // Thay thế hàm này với code của bạn để lưu file
//                //lưu vào đối tượng product trong DB => sẽ làm sau
//                //lưu vào bảng product_images
//            }
//            return ResponseEntity.ok("Product created successfully");
//        } catch (Exception e) {
//            return ResponseEntity.badRequest().body(e.getMessage());
//        }
//    }
//
//    private String storeFile(MultipartFile file) throws IOException {
//        String filename = StringUtils.cleanPath(file.getOriginalFilename());
//        // Thêm UUID vào trước tên file để đảm bảo tên file là duy nhất
//        String uniqueFilename = UUID.randomUUID().toString() + "_" + filename;
//
//        // Đường dẫn đến thư mục mà bạn muốn lưu file
//        java.nio.file.Path uploadDir = Paths.get("uploads"); // Nếu muốn lưu ở chỗ khác trên máy thì thế nào? Lưu hết trong project thì cứu sao nổi khi up lên Git:)))
//        // Kiểm tra và tạo thư mục nếu nó không tồn tại
//        if (!Files.exists(uploadDir)) {
//            Files.createDirectories(uploadDir);
//        }
//        // Đường dẫn đầy đủ đến file
//        java.nio.file.Path destination = Paths.get(uploadDir.toString(), uniqueFilename); // Ghép tên thư mục vào tên file để tạo đường dẫn hoàn chỉnh
//        // Sao chép file vào thư mục đích
//        Files.copy(file.getInputStream(), destination, StandardCopyOption.REPLACE_EXISTING);
//        return uniqueFilename;
//    }

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

            // Save thumbnail (If exists)
            List<MultipartFile> files = productDTO.getFiles();
            files = files == null ? new ArrayList<>() : files;

            for(MultipartFile file : files){
                Long fileSize = file.getSize();
                // Check size
                if(fileSize <= 0){
                    continue;
                }

                if(fileSize > 10 * 1024 * 1024){ // 10MB
                    responseDTO.setMessage("File's size cannot larger than 10MB!");
                    return ResponseEntity.status(HttpStatus.REQUEST_HEADER_FIELDS_TOO_LARGE).body(responseDTO);
                }

                String contentType = file.getContentType();
                // Check type
                if(contentType == null || !contentType.startsWith("image/")){
                    responseDTO.setMessage("This file's content type is not supported!");
                    return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE).body(responseDTO);
                }

                // get path's image after saving
                String imagePath = saveImage(file);
                productDTO.setThumbnail(productDTO.getThumbnail() + "," + imagePath);
            }

            responseDTO.setData(productDTO);

            return ResponseEntity.ok(responseDTO);
        }
        catch(Exception e){
            responseDTO.setMessage(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseDTO);
        }
    }

    // two paths of source path of file: file's path and folder's path
    public String saveImage(MultipartFile file) throws IOException {
        // Folder's path
        Path uploadDir = Paths.get(SystemConstant.imagePathPrefix);
        if(!Files.exists(uploadDir)){
            Files.createDirectories(uploadDir);
        }

        // File's path
        String fileName = file.getOriginalFilename();
        String fileNameEncoded = UUID.randomUUID() + "_" + fileName;
        Path destination = Paths.get(uploadDir.toString(), fileNameEncoded);

        Files.copy(file.getInputStream(), destination, StandardCopyOption.REPLACE_EXISTING);

        return fileNameEncoded;
    }

    @PutMapping("/{id}")
    public String updateProduct(@PathVariable("id") Long productId) {
        return "Update product by id = " + productId;
    }

    @DeleteMapping("/{id}")
    public String deleteProduct(@PathVariable("id") Long productId) {
        return "Delete product by id = " + productId;
    }
}
