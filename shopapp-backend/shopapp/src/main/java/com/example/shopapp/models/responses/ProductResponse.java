package com.example.shopapp.models.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductResponse extends BaseResponse {
    Long id;
    String name;
    Float price;
    String thumbnail;
    String description;

    @JsonProperty("category_id")
    Long categoryId;

    @JsonProperty("product_images")
    List<ProductImageResponse> productImageResponses;
}
