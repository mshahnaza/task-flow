package com.example.taskmanager.services.impl;

import com.example.taskmanager.dto.request.TaskRequest;
import com.example.taskmanager.dto.response.TaskResponse;
import com.example.taskmanager.entities.Category;
import com.example.taskmanager.entities.Task;
import com.example.taskmanager.mappers.TaskMapper;
import com.example.taskmanager.repositories.CategoryRepository;
import com.example.taskmanager.repositories.CommentRepository;
import com.example.taskmanager.repositories.TaskRepository;
import com.example.taskmanager.services.TaskService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class TaskServiceImpl implements TaskService {

    private final TaskMapper taskMapper;
    private final TaskRepository taskRepository;
    private final CategoryRepository categoryRepository;
    private final CommentRepository commentRepository;

    /**
     * Adds a new task.
     *
     * @param taskRequest The task data to be added.
     * @return The created task's response DTO.
     * @throws IllegalArgumentException If taskRequest is null.
     */
    @Override
    public TaskResponse addTask(TaskRequest taskRequest) {
        if(taskRequest == null) {
            throw new IllegalArgumentException("Task cannot be null.");
        }

        // Create a new Task entity
        Task task = Task.builder()
                .title(taskRequest.getTitle())
                .description(taskRequest.getDescription())
                .status(taskRequest.getStatus())
                .dueDate(taskRequest.getDueDate())
                .build();

        // Retrieve and assign categories
        List<Category> categories = categoryRepository.findAllById(taskRequest.getCategory_ids());
        if (categories == null || categories.isEmpty()) {
            task.setCategories(categories);
        }

        // Save the task and return the response DTO
        taskRepository.save(task);
        return taskMapper.taskToTaskDto(task);
    }

    /**
     * Retrieves all tasks with optional filtering and sorting.
     *
     * @param status     The status of the tasks to retrieve.
     * @param categoryIds The list of category IDs to filter by.
     * @param sortOrder  The sort order ("asc" or "desc").
     * @return A list of task response DTOs.
     * @throws EntityNotFoundException If no tasks are found with the specified criteria.
     */
    @Override
    public List<TaskResponse> getaAllTasks(String status, List<Long> categoryIds, String sortOrder) {
        List<Task> tasks;

        // Sort tasks based on comments count
        if (sortOrder.equals("asc")) {
            tasks = taskRepository.findTasksSortedByCommentsAsc(status, categoryIds);
        } else {
            tasks = taskRepository.findTasksSortedByCommentsDesc(status, categoryIds);
        }

        // Check if tasks are empty and throw exception if necessary
        if(tasks == null || tasks.isEmpty()) {
            throw new EntityNotFoundException("No tasks found for status: " + status);
        }

        // Map tasks to response DTOs, including comment counts
        return tasks.stream()
                .map(task -> taskMapper.taskToTaskDto(task, commentRepository.countByTask(task)))
                .collect(Collectors.toList());
    }

    /**
     * Retrieves a task by its ID.
     *
     * @param id The ID of the task to retrieve.
     * @return The task's response DTO.
     * @throws EntityNotFoundException If the task is not found with the specified ID.
     */
    @Override
    public TaskResponse getTaskById(Long id) {
        // Find the task by ID or throw an exception if not found
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Task not found with id: " + id));

        // Retrieve comment count for the task
        int commentCount = commentRepository.countByTask(task);

        // Map task to response DTO with comment count
        return taskMapper.taskToTaskDto(task, commentCount);
    }

    /**
     * Updates an existing task by its ID.
     *
     * @param id          The ID of the task to update.
     * @param taskRequest The task data to update.
     * @return The updated task's response DTO.
     * @throws IllegalArgumentException If task ID is null.
     * @throws EntityNotFoundException If the task is not found with the specified ID.
     */
    @Override
    public TaskResponse updateTask(Long id, TaskRequest taskRequest) {
        if (id == null) {
            throw new IllegalArgumentException("Task id cannot be null.");
        }

        // Find the task by ID or throw an exception if not found
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Task not found with id: " + id));

        // Update task fields if provided
        if (taskRequest.getTitle() != null) {
            task.setTitle(taskRequest.getTitle());
        }
        if (taskRequest.getDescription() != null) {
            task.setDescription(taskRequest.getDescription());
        }
        if (taskRequest.getStatus() != null) {
            task.setStatus(taskRequest.getStatus());
        }
        if (taskRequest.getDueDate() != null) {
            task.setDueDate(taskRequest.getDueDate());
        }

        if (taskRequest.getCategory_ids() != null && !taskRequest.getCategory_ids().isEmpty()) {
            List<Category> categories = categoryRepository.findAllById(taskRequest.getCategory_ids());
            task.setCategories(categories);
        }

        // Save the updated task and return the response DTO
        taskRepository.save(task);
        return taskMapper.taskToTaskDto(task, commentRepository.countByTask(task));
    }

    /**
     * Deletes a task by its ID.
     *
     * @param id The ID of the task to delete.
     * @throws EntityNotFoundException If the task is not found with the specified ID.
     */
    @Override
    public void deleteTask(Long id) {
        // Find the task by ID or throw an exception if not found
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Task not found with id: " + id));

        // Delete the task
        taskRepository.delete(task);
    }
}
