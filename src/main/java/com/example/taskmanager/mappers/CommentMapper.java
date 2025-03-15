package com.example.taskmanager.mappers;

import com.example.taskmanager.dto.response.CommentResponse;
import com.example.taskmanager.entities.Comment;
import com.example.taskmanager.entities.Task;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper
public interface CommentMapper {
    @Mapping(source = "task", target = "task_id", qualifiedByName = "taskToLong")
    CommentResponse commentToCommentDto(Comment comment);

    List<CommentResponse> commentToCommentDtos(List<Comment> comments);

    default Long taskToLong(Task task) {
        return task.getId();
    }
}
