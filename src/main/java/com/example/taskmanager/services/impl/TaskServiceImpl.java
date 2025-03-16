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

    @Override
    public TaskResponse addTask(TaskRequest taskRequest) {
        if(taskRequest != null || taskRequest.getTitle() != null || !(taskRequest.getTitle().isEmpty())) {
            Task task = new Task();
            task.setTitle(taskRequest.getTitle());
            task.setDescription(taskRequest.getDescription());
            task.setStatus(taskRequest.getStatus());
            task.setDueDate(taskRequest.getDueDate());

            List<Category> categories = categoryRepository.findAllById(taskRequest.getCategory_ids());
            if (categories != null && !categories.isEmpty()) {
                task.setCategories(categories);
            }

            taskRepository.save(task);
            return taskMapper.taskToTaskDto(task);
        }
        return null;
    }

    @Override
    public List<TaskResponse> getaAllTasks(String status, List<Long> categoryIds, String sortOrder) {
        List<Task> tasks;
        if (sortOrder.equals("asc")) {
            tasks = taskRepository.findTasksSortedByCommentsAsc(status, categoryIds);
        } else {
            tasks = taskRepository.findTasksSortedByCommentsDesc(status, categoryIds);
        }

        return tasks.stream()
                .map(task -> taskMapper.taskToTaskDto(task, commentRepository.countByTask(task)))
                .collect(Collectors.toList());
    }

    @Override
    public TaskResponse getTaskById(Long id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Task not found with id: " + id));

        int commentCount = commentRepository.countByTask(task);

        return taskMapper.taskToTaskDto(task, commentCount);
    }

    @Override
    public TaskResponse updateTask(Long id, TaskRequest taskRequest) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Task not found with id: " + id));

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

        taskRepository.save(task);
        return taskMapper.taskToTaskDto(task, commentRepository.countByTask(task));
    }

    @Override
    public void deleteTask(Long id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Task not found with id: " + id));

        taskRepository.delete(task);
    }
}
