package com.example.taskmanager.controllers;

import com.example.taskmanager.dto.request.CategoryRequest;
import com.example.taskmanager.dto.response.CategoryResponse;
import com.example.taskmanager.services.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/category")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @PostMapping("/add")
    public ResponseEntity<CategoryResponse> addCategory(@RequestBody CategoryRequest categoryRequest) {
        CategoryResponse response = categoryService.addCategory(categoryRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.ok("Category deleted successfully");
    }

    @GetMapping("/all")
    public List<CategoryResponse> getAllCategories() {
        return categoryService.getAllCategories();
    }
}
