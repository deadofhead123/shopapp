package com.example.shopapp.entities;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "social_accounts")
public class SocialAccount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(nullable = false, length = 20)
    String provider;

    @Column(name = "provider_id", nullable = false, length = 50)
    String providerId;

    @Column(nullable = false, length = 150)
    String email;

    @Column(nullable = false, length = 100)
    String name;

    @ManyToOne
    @JoinColumn(name = "user_id")
    User user;
}
