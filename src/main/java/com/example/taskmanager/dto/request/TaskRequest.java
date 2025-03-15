package com.example.taskmanager.dto.request;

import com.example.taskmanager.entities.Comment;
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
    private String title;
    private String description;
    private String status;
    private LocalDate dueDate;
    private List<Long> category_ids;
}
