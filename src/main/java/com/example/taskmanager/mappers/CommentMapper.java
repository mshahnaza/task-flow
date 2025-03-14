package com.example.taskmanager.mappers;

import com.example.taskmanager.dto.response.CommentResponse;
import com.example.taskmanager.entities.Comment;
import org.mapstruct.Mapper;

@Mapper
public interface CommentMapper {
    CommentResponse commentToCommentDto(Comment comment);
}
