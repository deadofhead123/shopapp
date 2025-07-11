package com.example.shopapp.models.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class UserLoginDTO {
    @JsonProperty("phone_number")
    @NotBlank(message = "Phone number cannot be null")
    String phoneNumber;

    @NotBlank(message = "Password cannot be null")
    @Size(min = 3, max = 200, message = "Password must have length in range of 3 - 200 characters")
    String password;
}
