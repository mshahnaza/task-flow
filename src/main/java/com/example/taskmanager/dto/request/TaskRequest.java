package com.example.taskmanager.dto.request;

import jakarta.validation.constraints.NotNull;
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
    @NotNull(message = "Title cannot be null.")
    private String title;
    private String description;

    @NotNull(message = "Status cannot be null.")
    private String status;
    private LocalDate dueDate;
    private List<Long> category_ids;
}
