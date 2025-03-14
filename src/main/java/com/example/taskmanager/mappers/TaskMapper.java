package com.example.taskmanager.mappers;

import com.example.taskmanager.dto.response.TaskResponse;
import com.example.taskmanager.entities.Task;
import org.mapstruct.Mapper;

@Mapper
public interface TaskMapper {
    TaskResponse taskToTaskDto(Task task);
}
