package com.example.taskmanager.security;

import com.example.taskmanager.entities.User;
import com.example.taskmanager.repositories.TaskRepository;
import com.example.taskmanager.repositories.UserRepository;
import com.example.taskmanager.services.UserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class TaskSecurity {

    private final TaskRepository taskRepository;
    private final UserService userService;

    public boolean isOwner(Long taskId) {
        User user = userService.getCurrentUser();

        return taskRepository.findById(taskId)
                .map(task -> task.getUser().getId().equals(user.getId()))
                .orElse(false);
    }
}
