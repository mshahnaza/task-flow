package com.example.taskmanager.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class TaskRequest {
    @NotNull(message = "Title cannot be null")
    @Size(min = 1, max = 20, message = "Title size cannot exceed 20 characters")
    private String title;
    private String description;

    @NotBlank(message = "Status cannot be blank")
    @NotNull(message = "Status cannot be null")
    private String status;
    private LocalDate dueDate;
    private List<Long> category_ids;
}
