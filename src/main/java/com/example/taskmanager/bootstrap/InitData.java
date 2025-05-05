package com.example.taskmanager.bootstrap;

import com.example.taskmanager.entities.Category;
import com.example.taskmanager.entities.Comment;
import com.example.taskmanager.entities.Task;
import com.example.taskmanager.entities.User;
import com.example.taskmanager.enums.Role;
import com.example.taskmanager.repositories.CategoryRepository;
import com.example.taskmanager.repositories.CommentRepository;
import com.example.taskmanager.repositories.TaskRepository;
import com.example.taskmanager.repositories.UserRepository;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class InitData {

    private final TaskRepository taskRepository;
    private final CategoryRepository categoryRepository;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @PostConstruct
    @Transactional
    public void init() {
        log.info("Initializing test data...");

        User user1 = User.builder()
                .username("user1")
                .email("user1@example.com")
                .password(passwordEncoder.encode("user"))
                .emailVerified(true)
                .roles(List.of(Role.ROLE_USER))
                .build();
        userRepository.save(user1);

        User user2 = User.builder()
                .username("user2")
                .email("user2@example.com")
                .password(passwordEncoder.encode("user"))
                .roles(List.of(Role.ROLE_USER))
                .emailVerified(true)
                .build();
        userRepository.save(user2);

        User admin1 = User.builder()
                .username("admin1")
                .email("admin1@example.com")
                .password(passwordEncoder.encode("admin"))
                .roles(List.of(Role.ROLE_ADMIN))
                .emailVerified(true)
                .build();
        userRepository.save(admin1);

        User admin2 = User.builder()
                .username("admin2")
                .email("admin2@example.com")
                .password(passwordEncoder.encode("admin"))
                .roles(List.of(Role.ROLE_ADMIN))
                .emailVerified(true)
                .build();
        userRepository.save(admin2);

        Category work = Category.builder().name("Work").user(user1).build();
        Category personal = Category.builder().name("Personal").user(user2).build();
        Category hobby = Category.builder().name("Hobby").user(admin2).build();

        categoryRepository.saveAll(List.of(work, personal, hobby));

        Task task1 = Task.builder()
                .title("Complete project")
                .description("Finish the Spring Boot backend")
                .status("In Progress")
                .dueDate(LocalDate.now().plusDays(5))
                .categories(List.of(work))
                .user(user1)
                .build();

        Task task2 = Task.builder()
                .title("Read a book")
                .description("Read 'Clean Code' by Robert C. Martin")
                .status("Finished")
                .dueDate(LocalDate.now().plusWeeks(2))
                .categories(List.of(personal, hobby))
                .user(user2)
                .build();

        taskRepository.saveAll(List.of(task1, task2));

        Comment comment1 = Comment.builder()
                .content("Need to finish this by next Monday.")
                .createdAt(LocalDateTime.now())
                .task(task1)
                .user(user1)
                .build();

        Comment comment2 = Comment.builder()
                .content("Great book! Focus on chapter 3.")
                .createdAt(LocalDateTime.now())
                .task(task2)
                .user(user2)
                .build();

        commentRepository.saveAll(List.of(comment1, comment2));
    }

    @PreDestroy
    @Transactional
    public void destroy() {
        commentRepository.deleteAll();
        taskRepository.deleteAll();
        categoryRepository.deleteAll();
        userRepository.deleteAll();
    }
}
