package com.example.shopapp.models.responses;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductResponse extends BaseResponse {
    String name;
    Float price;
    String thumbnail;
    String description;
    Long categoryId;
}
