package com.example.shopapp.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductDTO {
    @NotBlank(message = "Product's name cannot be null!")
    @Size(min = 3, max = 200, message = "Product name's length must be in range 3-200 characters!")
    String name;

    @Min(value = 1, message = "Product's price must larger than 0!")
    @Max(value = 10000000, message = "Product's price must smaller than 10.000.000!")
    Float price;

    String thumbnail;
    String description;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;

    @JsonProperty("category_id")
    Long categoryId;

    List<MultipartFile> files;
}
