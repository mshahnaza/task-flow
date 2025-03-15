package com.example.taskmanager.mappers;

import com.example.taskmanager.dto.response.TaskResponse;
import com.example.taskmanager.entities.Category;
import com.example.taskmanager.entities.Task;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper
public interface TaskMapper {

    List<TaskResponse> taskToTaskDtos(List<Task> tasks);

    default String categoryToString(Category category) {
        return category.getName();
    }

    @Mapping(target = "commentCount", ignore = true)
    TaskResponse taskToTaskDto(Task task);

    default TaskResponse taskToTaskDto(Task task, int commentCount) {
        TaskResponse response = taskToTaskDto(task);
        response.setCommentCount(commentCount);
        return response;
    }
}
