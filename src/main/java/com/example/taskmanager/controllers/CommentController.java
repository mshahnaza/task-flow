package com.example.taskmanager.controllers;

import com.example.taskmanager.dto.request.CommentRequest;
import com.example.taskmanager.dto.response.CommentResponse;
import com.example.taskmanager.services.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/comment")
@RequiredArgsConstructor
@Tag(name = "Comment Management", description = "APIs for managing task comments")
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/add/{id}")
    @Operation(
            summary = "Add a comment to a task",
            description = "Adds a comment to a task by task ID.",
            parameters = {@Parameter(name = "id", description = "ID of the task to add the comment to", required = true)},
            responses = {
                    @ApiResponse(responseCode = "201", description = "Comment successfully added",
                            content = @Content(schema = @Schema(implementation = CommentResponse.class))),
                    @ApiResponse(responseCode = "400", description = "Incorrect data")
            }
    )
    public ResponseEntity<CommentResponse> addComment(@PathVariable Long id, @Validated @RequestBody CommentRequest commentRequest) {
        CommentResponse response = commentService.addComment(id, commentRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @DeleteMapping("/delete/{id}")
    @Operation(
            summary = "Delete a comment",
            description = "Deletes a comment by ID.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Comment successfully deleted"),
                    @ApiResponse(responseCode = "404", description = "Comment not found")
            },
            parameters = {@Parameter(name = "id", description = "ID of the comment to delete", required = true)}
    )
    public ResponseEntity<String> deleteComment(@PathVariable Long id) {
        commentService.deleteComment(id);
        return ResponseEntity.ok("Comment deleted successfully");
    }

    @GetMapping("/all")
    @Operation(
            summary = "Get all comments",
            description = "Retrieves a list of all comments.",
            responses = {@ApiResponse(responseCode = "200", description = "List of comments successfully retrieved",
                    content = @Content(schema = @Schema(implementation = CommentResponse.class, type = "array")))}
    )
    public List<CommentResponse> getAllComments() {
        return commentService.getaAllComments();
    }

    @GetMapping("/task/{id}")
    @Operation(
            summary = "Get comments for a task",
            description = "Retrieves a list of all comments associated with a specific task.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Comments successfully found",
                            content = @Content(schema = @Schema(implementation = CommentResponse.class, type = "array"))),
                    @ApiResponse(responseCode = "404", description = "Task not found")
            },
            parameters = {@Parameter(name = "id", description = "ID of the task to retrieve comments for", required = true) })
    public List<CommentResponse> getTaskComments(@PathVariable Long id) {
        return commentService.getaAllCommentsByTaskId(id);
    }
}