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
        if (commentRequest != null || taskId != null || commentRequest.getContent() != null || !commentRequest.getContent().isEmpty()) {
            Comment comment = new Comment();
            comment.setContent(commentRequest.getContent());
            comment.setCreatedAt(LocalDateTime.now());

            Task task = taskRepository.findById(taskId)
                    .orElseThrow(() -> new EntityNotFoundException("Task not found with id: " + taskId));
            comment.setTask(task);

            commentRepository.save(comment);
            return commentMapper.commentToCommentDto(comment);
        }
        return null;
    }

    @Override
    public List<CommentResponse> getaAllComments() {
        return commentMapper.commentToCommentDtos(commentRepository.findAll());
    }

    @Override
    public void deleteComment(Long id) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Comment not found with id: " + id));

        commentRepository.delete(comment);
    }

    @Override
    public List<CommentResponse> getaAllCommentsByTaskId(Long taskId) {
        List<Comment> comments = commentRepository.findByTaskId(taskId); // Получаем комментарии по taskId
        return commentMapper.commentToCommentDtos(comments);
    }
}
