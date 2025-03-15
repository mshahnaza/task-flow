package com.example.taskmanager.controllers;

import com.example.taskmanager.dto.request.TaskRequest;
import com.example.taskmanager.dto.response.TaskResponse;
import com.example.taskmanager.services.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/task")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    @PostMapping("/add")
    public void addTask(@RequestBody TaskRequest taskRequest) {
        taskService.addTask(taskRequest);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
    }

    @PatchMapping("/update/{id}")
    public void updateTask(@PathVariable Long id, @RequestBody TaskRequest taskRequest) {
        taskService.updateTask(id, taskRequest);
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
