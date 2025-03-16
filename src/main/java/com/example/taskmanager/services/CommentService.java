package com.example.taskmanager.services;

import com.example.taskmanager.dto.request.CommentRequest;
import com.example.taskmanager.dto.response.CommentResponse;

import java.util.List;

public interface CommentService {
    CommentResponse addComment(Long taskId, CommentRequest commentRequest);

    List<CommentResponse> getaAllComments();

    void deleteComment(Long commentId);

    List<CommentResponse> getaAllCommentsByTaskId(Long taskId);
}
