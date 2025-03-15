package com.example.taskmanager.mappers;

import com.example.taskmanager.dto.response.CategoryResponse;
import com.example.taskmanager.entities.Category;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper
public interface CategoryMapper {
    List<CategoryResponse> categoryToCategoryDtos(List<Category> categories);

    CategoryResponse categoryToCategoryDto(Category category);
}
