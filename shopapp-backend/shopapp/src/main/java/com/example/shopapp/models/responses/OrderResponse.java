package com.example.shopapp.models.responses;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.Date;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderResponse {
    Long userId;
    String fullName;
    String email;
    String phoneNumber;
    String address;
    String note;
    Date orderDate;
    String status;
    Integer totalMoney;
    String shippingMethod;
    String shippingAddress;
    LocalDate shippingDate;
    String trackingNumber;
    String paymentMethod;
}
