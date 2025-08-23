package com.example.shopapp.services.category;

import com.example.shopapp.constant.MessageKeys;
import com.example.shopapp.models.dtos.CategoryDTO;
import com.example.shopapp.entities.Category;
import com.example.shopapp.repositories.CategoryRepository;
import com.example.shopapp.utils.LocalizationUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class CategoryService implements ICategoryService {
    private final CategoryRepository categoryRepository;
    private final LocalizationUtil localizationUtil;

    @Override
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    @Override
    public Category getCategoryById(Long id) {
        return categoryRepository.findById(id).orElseThrow(() -> new RuntimeException(localizationUtil.getLocalizedMessage(MessageKeys.WRONG_CATEGORY_ID, id)));
    }

    @Override
    public Category createCategory(CategoryDTO categoryDTO) {
        return categoryRepository.save(Category.builder().name(categoryDTO.getName()).build());
    }

    @Override
    public Category updateCategory(Long id, CategoryDTO categoryDTO) {
        Category existingCategory = getCategoryById(id);
        existingCategory.setName(categoryDTO.getName());

        return categoryRepository.save(existingCategory);
    }

    @Override
    public Boolean deleteCategory(Long id) {
        Category existingCategory = getCategoryById(id);
        categoryRepository.delete(existingCategory);
        return true;
    }
}
