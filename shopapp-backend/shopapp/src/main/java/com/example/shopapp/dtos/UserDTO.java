package com.example.shopapp.dtos;

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
public class UserDTO {
    @JsonProperty("fullname")
    String fullName;

    @JsonProperty("phone_number")
    @NotBlank(message = "Phone number cannot be null")
    String phoneNumber;

    String address;

    @NotBlank(message = "Password cannot be null")
    @Size(min = 3, max = 200, message = "Password must have length in range of 3 - 200 characters")
    String password;

    @NotBlank(message = "Retype password cannot be null")
    String retypePassword;

    @JsonProperty("date_of_birth")
    String dateOfBirth;

    @JsonProperty("facebook_account_id")
    Integer facebookAccountId;

    @JsonProperty("google_account_id")
    Integer googleAccountId;

    @JsonProperty("role_id")
    Long roleId;
}
