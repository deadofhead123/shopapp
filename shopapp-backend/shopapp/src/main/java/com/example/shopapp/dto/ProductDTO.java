package com.example.shopapp.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.Date;

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
    Date createdAt;
    Date updatedAt;
    Integer categoryId;
}
