package com.example.shopapp.models.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductImageDTO {
    @JsonProperty
    @Min(value = 1, message = "Product's id must > 0")
    Long productId;

    @JsonProperty
    @Size(min = 1, max = 300, message = "Image url's length must be in range of 1-300 chars")
    String imageUrl;
}
