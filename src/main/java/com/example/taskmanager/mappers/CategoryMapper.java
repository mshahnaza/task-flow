package com.example.taskmanager.mappers;

import com.example.taskmanager.dto.response.CategoryResponse;
import com.example.taskmanager.entities.Category;
import org.mapstruct.Mapper;

@Mapper
public interface CategoryMapper {
    CategoryResponse categoryToCategoryDto(Category category);
}
