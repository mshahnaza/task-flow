package com.example.taskmanager.controllers;

import com.example.taskmanager.dto.request.CommentRequest;
import com.example.taskmanager.dto.response.CommentResponse;
import com.example.taskmanager.services.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/comment")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/add/{id}")
    public void addComment(@PathVariable Long id, @RequestBody CommentRequest commentRequest) {
        commentService.addComment(id,commentRequest);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteComment(@PathVariable Long id) {
        commentService.deleteComment(id);
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
