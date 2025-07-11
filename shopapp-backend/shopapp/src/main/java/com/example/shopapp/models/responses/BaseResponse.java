package com.example.shopapp.models.responses;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
public class BaseResponse {
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
}
