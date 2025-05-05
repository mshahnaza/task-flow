package com.example.taskmanager.mappers;

import com.example.taskmanager.dto.response.CategoryResponse;
import com.example.taskmanager.entities.Category;
import com.example.taskmanager.entities.User;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
public class TestCategoryMapper {
    private CategoryMapper categoryMapper;

    @Test
    void testCategoryToCategoryDto() {
        categoryMapper = new CategoryMapperImpl();

        Category category = new Category(1L, "Test Category", null, null);

        CategoryResponse categoryResponse = categoryMapper.categoryToCategoryDto(category);

        assertEquals(category.getId(), categoryResponse.getId());
        assertEquals(category.getName(), categoryResponse.getName());
    }
}
