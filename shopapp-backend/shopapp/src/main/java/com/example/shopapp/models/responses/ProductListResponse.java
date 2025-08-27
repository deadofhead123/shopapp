package com.example.shopapp.models.responses;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductListResponse {
    int totalPages;
    List<ProductResponse> products;
}
