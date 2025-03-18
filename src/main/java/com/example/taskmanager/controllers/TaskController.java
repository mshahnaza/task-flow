package com.example.taskmanager.controllers;

import com.example.taskmanager.dto.request.TaskRequest;
import com.example.taskmanager.dto.response.TaskResponse;
import com.example.taskmanager.services.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/task")
@RequiredArgsConstructor
@Tag(name = "Task Management", description = "APIs for managing tasks")
public class TaskController {

    private final TaskService taskService;

    @PostMapping("/add")
    @Operation(
            summary = "Add a new task",
            description = "Creates a new task.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Task successfully created",
                            content = @Content(schema = @Schema(implementation = TaskResponse.class))),
                    @ApiResponse(responseCode = "400", description = "Incorrect data")
            },
            requestBody = @RequestBody(
                    description = "Task data",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = TaskRequest.class))
            )
    )
    public ResponseEntity<TaskResponse> addTask(@Validated @RequestBody TaskRequest taskRequest) {
        TaskResponse response = taskService.addTask(taskRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @DeleteMapping("/delete/{id}")
    @Operation(
            summary = "Delete a task",
            description = "Deletes a task by ID.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Task successfully deleted"),
                    @ApiResponse(responseCode = "404", description = "Task not found")
            },
            parameters = @Parameter(description = "Task ID to delete")
    )
    public ResponseEntity<String> deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
        return ResponseEntity.ok("Task deleted successfully");
    }

    @PatchMapping("/update/{id}")
    @Operation(
            summary = "Update a task",
            description = "Updates an existing task with new data.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Task successfully updated",
                            content = @Content(schema = @Schema(implementation = TaskResponse.class))),
                    @ApiResponse(responseCode = "404", description = "Task not found")
            },
            parameters = @Parameter(description = "Task ID to update"),
            requestBody = @RequestBody(
                    description = "New task data",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = TaskRequest.class))
            )
    )
    public ResponseEntity<TaskResponse> updateTask(@PathVariable Long id, @RequestBody TaskRequest taskRequest) {
        TaskResponse response = taskService.updateTask(id, taskRequest);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Get task by ID",
            description = "Fetches a task using its ID.",
            responses = {
                @ApiResponse(responseCode = "200", description = "Task successfully retrieved",
                    content = @Content(schema = @Schema(implementation = TaskResponse.class))),
                @ApiResponse(responseCode = "404", description = "Task not found")
            },
            parameters = @Parameter(description = "Task ID to retrieve")
    )
    public TaskResponse getTaskById(@PathVariable Long id) {
        return taskService.getTaskById(id);
    }

    @GetMapping("/all")
    @Operation(
            summary = "Get all tasks",
            description = "Retrieves all tasks with optional filters.",
            responses = {@ApiResponse(responseCode = "200", description = "Tasks list successfully retrieved")},
            parameters = {
                    @Parameter(name = "status", description = "Filter tasks by status (e.g., 'completed', 'pending')", required = false),
                    @Parameter(name = "categoryIds", description = "Filter tasks by category IDs (comma-separated)", required = false),
                    @Parameter(name = "sortOrder", description = "Sorting order: 'asc' or 'desc' (default: 'desc')", required = false, example = "desc")
            }
    )
    public List<TaskResponse> getAllTasks(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) List<Long> categoryIds,
            @RequestParam(defaultValue = "desc") String sortOrder) {
        return taskService.getaAllTasks(status, categoryIds, sortOrder);
    }
}
