package com.example.shopapp.entities;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "fullname", length = 100)
    String fullName;

    @Column(name = "phone_number", length = 10)
    String phoneNumber;

    @Column(length = 200)
    String address;

    @Column(nullable = false, length = 100)
    String password;

    @Column(name = "is_active")
    Boolean isActive;

    @Column(name = "date_of_birth")
    String dateOfBirth;

    @Column(name = "facebook_account_id")
    Integer facebookAccountId;

    @Column(name = "google_account_id")
    Integer googleAccountId;

    @ManyToOne
    @JoinColumn(name = "role_id")
    Role role;
}
