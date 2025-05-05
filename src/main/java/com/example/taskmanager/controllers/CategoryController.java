package com.example.taskmanager.controllers;

import com.example.taskmanager.dto.request.CategoryRequest;
import com.example.taskmanager.dto.response.CategoryResponse;
import com.example.taskmanager.services.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/category")
@RequiredArgsConstructor
@Tag(name = "Category Management", description = "APIs for managing task categories")
public class CategoryController {

    private final CategoryService categoryService;

    @PostMapping("/add")
    @Operation(
            summary = "Add a new category",
            description = "Creates a new task category.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Category successfully created",
                            content = @Content(schema = @Schema(implementation = CategoryResponse.class))),
                    @ApiResponse(responseCode = "400", description = "Incorrect data")
            }
    )
    public ResponseEntity<CategoryResponse> addCategory(@Validated @RequestBody CategoryRequest categoryRequest) {
        CategoryResponse response = categoryService.addCategory(categoryRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @DeleteMapping("/delete/{id}")
    @Operation(
            summary = "Delete a category",
            description = "Deletes a category by ID.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Category successfully deleted"),
                    @ApiResponse(responseCode = "404", description = "Category not found")
            },
            parameters = {@Parameter(name = "id", description = "ID of the category to delete", required = true)}
    )
    public ResponseEntity<String> deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.ok("Category deleted successfully");
    }

    @GetMapping("/all")
    @Operation(
            summary = "Get all categories",
            description = "Retrieves a list of all task categories.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "The list of categories has been successfully retrieved")
            })
    public List<CategoryResponse> getAllCategories() {
        return categoryService.getAllCategories();
    }
}