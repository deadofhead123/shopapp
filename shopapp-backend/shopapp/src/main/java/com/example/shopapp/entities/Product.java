package com.example.shopapp.entities;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "products")
public class Product extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(length = 350)
    String name;

    @Column(nullable = false)
    Float price;

    @Column(length = 300)
    String thumbnail;

    String description;

    @ManyToOne
    @JoinColumn(name = "category_id")
    Category category;
}
