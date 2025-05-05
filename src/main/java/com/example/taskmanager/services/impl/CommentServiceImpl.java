package com.example.taskmanager.services.impl;

import com.example.taskmanager.dto.request.CommentRequest;
import com.example.taskmanager.dto.response.CommentResponse;
import com.example.taskmanager.entities.Comment;
import com.example.taskmanager.entities.Task;
import com.example.taskmanager.entities.User;
import com.example.taskmanager.mappers.CommentMapper;
import com.example.taskmanager.repositories.CommentRepository;
import com.example.taskmanager.repositories.TaskRepository;
import com.example.taskmanager.services.CommentService;
import com.example.taskmanager.services.UserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final TaskRepository taskRepository;
    private final CommentMapper commentMapper;
    private final UserService userService;

    /**
     * Adds a new comment to a task.
     *
     * @param taskId         The ID of the task to which the comment will be added.
     * @param commentRequest The comment data to be added.
     * @return The created comment's response DTO.
     * @throws IllegalArgumentException If taskId or commentRequest is null.
     * @throws EntityNotFoundException If no task is found with the given taskId.
     */
    @Override
    public CommentResponse addComment(Long taskId, CommentRequest commentRequest) {
        if (commentRequest == null || taskId == null) {
            throw new IllegalArgumentException("Comment or task id cannot be null.");
        }

        // Find the task by taskId, throw an exception if not found
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new EntityNotFoundException("Task not found with id: " + taskId));

        User user = userService.getCurrentUser();
        if (!task.getUser().getId().equals(user.getId())) {
            throw new AccessDeniedException("Access denied to this task");
        }

        // Create and save the new comment
        Comment comment = Comment.builder()
                .content(commentRequest.getContent())
                .createdAt(LocalDateTime.now())
                .task(task)
                .user(user)
                .build();

        commentRepository.save(comment);

        // Map the comment entity to the response DTO
        return commentMapper.commentToCommentDto(comment);
    }

    /**
     * Retrieves all comments.
     *
     * @return A list of comment response DTOs.
     * @throws EntityNotFoundException If no comments are found.
     */
    @Override
    public List<CommentResponse> getaAllComments() {
        User user = userService.getCurrentUser();
        List<Comment> comments = commentRepository.findAllByUser(user);

        // If no comments are found, throw an exception
        if (comments.isEmpty()) {
            throw new EntityNotFoundException("No comments found.");
        }

        // Map and return the list of comments
        return commentMapper.commentToCommentDtos(comments);
    }

    /**
     * Deletes a comment by its ID.
     *
     * @param id The ID of the comment to be deleted.
     * @throws EntityNotFoundException If the comment is not found with the given ID.
     */
    @Override
    @PreAuthorize("@commentSecurity.isOwner(#id)")
    public void deleteComment(Long id) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Comment not found with id: " + id));

        // Delete the found comment
        commentRepository.delete(comment);
    }

    /**
     * Retrieves all comments for a specific task by its ID.
     *
     * @param taskId The ID of the task to retrieve comments for.
     * @return A list of comment response DTOs.
     * @throws IllegalArgumentException If taskId is null.
     * @throws EntityNotFoundException If no comments are found for the task with the given taskId.
     */
    @Override
    public List<CommentResponse> getaAllCommentsByTaskId(Long taskId) {
        if (taskId == null) {
            throw new IllegalArgumentException("Task ID cannot be null.");
        }

        User user = userService.getCurrentUser();
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new EntityNotFoundException("Task not found"));

        if (!task.getUser().getId().equals(user.getId())) {
            throw new AccessDeniedException("Access denied to this task");
        }

        // Find comments for the task by taskId
        List<Comment> comments = commentRepository.findByTaskId(taskId);

        // If no comments are found, throw an exception
        if (comments.isEmpty()) {
            throw new EntityNotFoundException("No comments found for task with id: " + taskId);
        }

        // Map and return the list of comments for the task
        return commentMapper.commentToCommentDtos(comments);
    }
}
