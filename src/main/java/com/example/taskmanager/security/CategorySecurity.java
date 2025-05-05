package com.example.taskmanager.security;

import com.example.taskmanager.entities.User;
import com.example.taskmanager.repositories.CategoryRepository;
import com.example.taskmanager.repositories.UserRepository;
import com.example.taskmanager.services.UserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class CategorySecurity {

    private final CategoryRepository categoryRepository;
    private final UserService userService;

    public boolean isOwner(Long categoryId) {
        User user = userService.getCurrentUser();

        if (!categoryRepository.existsById(categoryId)) {
            throw new EntityNotFoundException("Category with ID " + categoryId + " not found");
        }

        return categoryRepository.findById(categoryId)
                .map(category -> category.getUser().getId().equals(user.getId()))
                .orElse(false);
    }
}
