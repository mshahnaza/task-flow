package com.example.taskmanager.controllers;

import com.example.taskmanager.dto.request.TaskRequest;
import com.example.taskmanager.dto.response.TaskResponse;
import com.example.taskmanager.services.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/task")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    @PostMapping("/add")
    public ResponseEntity<TaskResponse> addTask(@RequestBody @Validated TaskRequest taskRequest) {
        TaskResponse response = taskService.addTask(taskRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
        return ResponseEntity.ok("Task deleted successfully");
    }

    @PatchMapping("/update/{id}")
    public ResponseEntity<TaskResponse> updateTask(@PathVariable Long id, @RequestBody TaskRequest taskRequest) {
        TaskResponse response = taskService.updateTask(id, taskRequest);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public TaskResponse getTaskById(@PathVariable Long id) {
        return taskService.getTaskById(id);
    }

    @GetMapping("/all")
    public List<TaskResponse> getAllTasks(@RequestParam(required = false) String status,
                                          @RequestParam(required = false) List<Long> categoryIds,
                                          @RequestParam(defaultValue = "desc") String sortOrder) {
        return taskService.getaAllTasks(status, categoryIds, sortOrder);
    }
}
