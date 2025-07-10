package com.example.shopapp.services.category;

import com.example.shopapp.dtos.CategoryDTO;
import com.example.shopapp.entities.Category;

import java.util.List;

public interface ICategoryService {
    List<Category> getAllCategories();

    Category getCategoryById(Long id);

    Category createCategory(CategoryDTO categoryDTO);

    Category updateCategory(Long id, CategoryDTO categoryDTO);

    Boolean deleteCategory(Long id);
}
