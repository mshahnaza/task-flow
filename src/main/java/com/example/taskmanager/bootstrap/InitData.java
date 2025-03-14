package com.example.taskmanager.bootstrap;

import com.example.taskmanager.entities.Category;
import com.example.taskmanager.entities.Comment;
import com.example.taskmanager.entities.Task;
import com.example.taskmanager.repositories.CategoryRepository;
import com.example.taskmanager.repositories.CommentRepository;
import com.example.taskmanager.repositories.TaskRepository;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
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

    @PostConstruct
    @Transactional
    public void init() {
        log.info("Initializing test data...");

        Category work = Category.builder().name("Work").build();
        Category personal = Category.builder().name("Personal").build();
        Category hobby = Category.builder().name("Hobby").build();

        categoryRepository.saveAll(List.of(work, personal, hobby));

        Task task1 = Task.builder()
                .title("Complete project")
                .description("Finish the Spring Boot backend")
                .status("In Progress")
                .dueDate(LocalDate.now().plusDays(5))
                .categories(List.of(work))
                .build();

        Task task2 = Task.builder()
                .title("Read a book")
                .description("Read 'Clean Code' by Robert C. Martin")
                .status("Finished")
                .dueDate(LocalDate.now().plusWeeks(2))
                .categories(List.of(personal, hobby))
                .build();

        taskRepository.saveAll(List.of(task1, task2));

        Comment comment1 = Comment.builder()
                .content("Need to finish this by next Monday.")
                .createdAt(LocalDateTime.now())
                .task(task1)
                .build();

        Comment comment2 = Comment.builder()
                .content("Great book! Focus on chapter 3.")
                .createdAt(LocalDateTime.now())
                .task(task2)
                .build();

        commentRepository.saveAll(List.of(comment1, comment2));
    }

    @PreDestroy
    @Transactional
    public void destroy() {
        commentRepository.deleteAll();
        taskRepository.deleteAll();
        categoryRepository.deleteAll();
    }
}
