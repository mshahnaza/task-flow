package com.example.taskmanager.services.impl;

import com.example.taskmanager.dto.request.CommentRequest;
import com.example.taskmanager.dto.response.CommentResponse;
import com.example.taskmanager.entities.Comment;
import com.example.taskmanager.entities.Task;
import com.example.taskmanager.mappers.CommentMapper;
import com.example.taskmanager.repositories.CommentRepository;
import com.example.taskmanager.repositories.TaskRepository;
import com.example.taskmanager.services.CommentService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;

    private final TaskRepository taskRepository;

    private final CommentMapper commentMapper;

    @Override
    public CommentResponse addComment(Long taskId, CommentRequest commentRequest) {
        if (commentRequest == null || taskId == null || commentRequest.getContent() == null || commentRequest.getContent().isEmpty()) {
            throw new IllegalArgumentException("Comment content cannot be null or empty.");
        }
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new EntityNotFoundException("Task not found with id: " + taskId));

        Comment comment = Comment.builder()
            .content(commentRequest.getContent())
            .createdAt(LocalDateTime.now())
                .task(task)
                .build();

        commentRepository.save(comment);
        return commentMapper.commentToCommentDto(comment);
    }

    @Override
    public List<CommentResponse> getaAllComments() {
        List<Comment> comments = commentRepository.findAll();

        if (comments.isEmpty()) {
            throw new EntityNotFoundException("No comments found.");
        }

        return commentMapper.commentToCommentDtos(comments);
    }

    @Override
    public void deleteComment(Long id) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Comment not found with id: " + id));

        commentRepository.delete(comment);
    }

    @Override
    public List<CommentResponse> getaAllCommentsByTaskId(Long taskId) {
        if (taskId == null) {
            throw new IllegalArgumentException("Task ID cannot be null.");
        }

        List<Comment> comments = commentRepository.findByTaskId(taskId);

        if (comments.isEmpty()) {
            throw new EntityNotFoundException("No comments found for task with id: " + taskId);
        }

        return commentMapper.commentToCommentDtos(comments);
    }
}
