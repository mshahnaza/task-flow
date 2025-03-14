package com.example.taskmanager.mappers;

import com.example.taskmanager.dto.response.TaskResponse;
import com.example.taskmanager.entities.Category;
import com.example.taskmanager.entities.Task;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper
public interface TaskMapper {
    TaskResponse taskToTaskDto(Task task);

    List<TaskResponse> taskToTaskDtos(List<Task> tasks);

    default String categoryToString(Category category) {
        return category.getName();
    }
}
