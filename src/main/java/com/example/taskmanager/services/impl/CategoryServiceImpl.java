package com.example.taskmanager.services.impl;

import com.example.taskmanager.dto.request.CategoryRequest;
import com.example.taskmanager.dto.response.CategoryResponse;
import com.example.taskmanager.entities.Category;
import com.example.taskmanager.mappers.CategoryMapper;
import com.example.taskmanager.repositories.CategoryRepository;
import com.example.taskmanager.services.CategoryService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    private final CategoryMapper categoryMapper;

    @Override
    public void addCategory(CategoryRequest categoryRequest) {
        if(categoryRequest != null || categoryRequest.getName() != null || !(categoryRequest.getName().isEmpty())) {
            Category category = new Category();
            category.setName(categoryRequest.getName());

            categoryRepository.save(category);
        }
    }

    @Override
    public List<CategoryResponse> getAllCategories() {
        return categoryMapper.categoryToCategoryDtos(categoryRepository.findAll());
    }

    @Override
    public void deleteCategory(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Category not found with id: " + id));

        categoryRepository.delete(category);
    }
}
