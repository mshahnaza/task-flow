package com.example.taskmanager.mappers;

import com.example.taskmanager.dto.response.CommentResponse;
import com.example.taskmanager.entities.Comment;
import com.example.taskmanager.entities.Task;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

@Mapper
public interface CommentMapper {
    @Mapping(source = "task", target = "task_id", qualifiedByName = "taskToLong")
    CommentResponse commentToCommentDto(Comment comment);

    List<CommentResponse> commentToCommentDtos(List<Comment> comments);

    @Named("taskToLong")
    default Long taskToLong(Task task) {
        return task != null ? task.getId() : null; // Добавил проверку на null
    }
}
