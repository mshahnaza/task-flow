package com.example.taskmanager.security;

import com.example.taskmanager.entities.User;
import com.example.taskmanager.repositories.CommentRepository;
import com.example.taskmanager.repositories.UserRepository;
import com.example.taskmanager.services.UserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class CommentSecurity {

    private final CommentRepository commentRepository;
    private final UserService userService;

    public boolean isOwner(Long commentId) {
        User user = userService.getCurrentUser();

        if (!commentRepository.existsById(commentId)) {
            throw new EntityNotFoundException("Comment with ID " + commentId + " not found");
        }

        return commentRepository.findById(commentId)
                .map(comment -> comment.getUser().getId().equals(user.getId()))
                .orElse(false);
    }
}
