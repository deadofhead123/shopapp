package com.example.shopapp.repositories;

import com.example.shopapp.entities.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProductRepository extends JpaRepository<Product, Long> {
    boolean existsByName(String name);

    // ":categoryId IS NULL OR :categoryId = 0": Cái WHERE 1=1 trong khóa Java Backend bên 28tech ấy:)))))
    @Query(
            value = " SELECT p " +
                    " FROM Product p " +
                    " WHERE (:keyword IS NULL OR :keyword = '' OR p.name LIKE %:keyword% OR p.description LIKE %:keyword%) " +
                    " AND (:categoryId IS NULL OR :categoryId = 0 OR p.category.id = :categoryId) "
    )
    Page<Product> findAll(
            @Param("keyword") String keyword,
            @Param("categoryId") Integer categoryId,
            Pageable pageable
    );//phân trang

}
