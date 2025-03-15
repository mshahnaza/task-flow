package com.example.taskmanager.services;

import com.example.taskmanager.dto.request.CategoryRequest;
import com.example.taskmanager.dto.response.CategoryResponse;

import java.util.List;

public interface CategoryService {
    void addCategory(CategoryRequest categoryRequest);

    List<CategoryResponse> getAllCategories();

    void deleteCategory(Long id);
}
