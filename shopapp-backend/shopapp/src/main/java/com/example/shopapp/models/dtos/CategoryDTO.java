package com.example.shopapp.models.dtos;

import jakarta.validation.constraints.NotEmpty;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CategoryDTO {
    @NotEmpty(message = "Category's name cannot be null!")
    String name;
}
