package com.example.taskmanager.services.impl;

import com.example.taskmanager.dto.request.CategoryRequest;
import com.example.taskmanager.dto.response.CategoryResponse;
import com.example.taskmanager.entities.Category;
import com.example.taskmanager.entities.User;
import com.example.taskmanager.mappers.CategoryMapper;
import com.example.taskmanager.repositories.CategoryRepository;
import com.example.taskmanager.services.CategoryService;
import com.example.taskmanager.services.UserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;
    private final UserService userService;

    /**
     * Adds a new category to the database.
     *
     * @param categoryRequest the request object containing category details
     * @return CategoryResponse containing details of the created category
     * @throws IllegalArgumentException if the categoryRequest is null
     */
    @Override
    public CategoryResponse addCategory(CategoryRequest categoryRequest) {
        if (categoryRequest == null) {
            throw new IllegalArgumentException("Category cannot be null");
        }

        User user = userService.getCurrentUser();

        // Creating a new category entity
        Category category = Category.builder()
                .name(categoryRequest.getName())
                .user(user)
                .build();

        // Saving the category in the repository
        categoryRepository.save(category);

        // Mapping entity to response DTO
        return categoryMapper.categoryToCategoryDto(category);
    }

    /**
     * Retrieves all categories from the database.
     *
     * @return a list of CategoryResponse DTOs
     * @throws EntityNotFoundException if no categories are found
     */
    @Override
    public List<CategoryResponse> getAllCategories() {
        // Fetching all categories from the repository
        List<Category> categories = categoryRepository.findAll();

        // Throw an exception if no categories exist
        if (categories.isEmpty()) {
            throw new EntityNotFoundException("No categories found.");
        }

        // Mapping entities to response DTOs
        return categoryMapper.categoryToCategoryDtos(categories);
    }

    /**
     * Deletes a category by its ID.
     *
     * @param id the ID of the category to delete
     * @throws EntityNotFoundException if the category is not found
     */
    @Override
    @PreAuthorize("(@categorySecurity.isOwner(#id) or hasRole('ADMIN'))")
    public void deleteCategory(Long id) {
        // Find the category by ID or throw an exception if not found
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Category not found with id: " + id));

        // Deleting the category from the repository
        categoryRepository.delete(category);
    }
}
