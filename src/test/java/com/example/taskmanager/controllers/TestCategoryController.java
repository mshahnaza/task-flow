package com.example.taskmanager.controllers;

import com.example.taskmanager.dto.request.CategoryRequest;
import com.example.taskmanager.dto.response.CategoryResponse;
import com.example.taskmanager.services.CategoryService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.Arrays;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = CategoryController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
public class TestCategoryController {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CategoryService categoryService;

    @Autowired
    private ObjectMapper objectMapper;

    private CategoryRequest categoryRequest;
    private CategoryResponse categoryResponse;

    @BeforeEach
    public void init() {
        categoryRequest = new CategoryRequest("Work");
        categoryResponse = new CategoryResponse(1L, "Work");
    }

    @Test
    public void CategoryController_AddCategory_ReturnsCreated() throws Exception {
        given(categoryService.addCategory(ArgumentMatchers.any())).willReturn(categoryResponse);

        ResultActions response = mockMvc.perform(post("/category/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(categoryRequest)));

        response.andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(categoryResponse.getId()))
                .andExpect(jsonPath("$.name").value(categoryResponse.getName()));
    }

    @Test
    public void CategoryController_DeleteCategory_ReturnsSuccessMessage() throws Exception {
        doNothing().when(categoryService).deleteCategory(1L);

        ResultActions response = mockMvc.perform(delete("/category/delete/1")
                .contentType(MediaType.APPLICATION_JSON));

        response.andExpect(status().isOk())
                .andExpect(content().string("Category deleted successfully"));
    }

    @Test
    public void CategoryController_GetAllCategories_ReturnsCategoryList() throws Exception {
        List<CategoryResponse> categoryList = Arrays.asList(categoryResponse);
        given(categoryService.getAllCategories()).willReturn(categoryList);

        ResultActions response = mockMvc.perform(get("/category/all")
                .contentType(MediaType.APPLICATION_JSON));

        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(categoryList.size()));
    }
}
