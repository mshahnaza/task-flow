package com.example.taskmanager.controllers;

import com.example.taskmanager.dto.request.CommentRequest;
import com.example.taskmanager.dto.response.CommentResponse;
import com.example.taskmanager.services.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/comment")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/add/{id}")
    public ResponseEntity<CommentResponse> addComment(@PathVariable Long id, @RequestBody CommentRequest commentRequest) {
        CommentResponse response = commentService.addComment(id,commentRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteComment(@PathVariable Long id) {
        commentService.deleteComment(id);
        return ResponseEntity.ok("Comment deleted successfully");
    }

    @GetMapping("/all")
    public List<CommentResponse> getAllComments() {
        return commentService.getaAllComments();
    }

    @GetMapping("/task/{id}")
    public List<CommentResponse> getTaskComments(@PathVariable Long id) {
        return commentService.getaAllCommentsByTaskId(id);
    }
}
