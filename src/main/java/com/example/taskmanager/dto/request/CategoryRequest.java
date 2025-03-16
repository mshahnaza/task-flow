package com.example.taskmanager.dto.request;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;


import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class CategoryRequest {
    @NotNull(message = "Category name cannot be null")
    @NotBlank(message = "Category cannot be blank")
    @Size(min = 1, max = 15, message = "Name size cannot exceed 15 characters")
    private String name;
}
