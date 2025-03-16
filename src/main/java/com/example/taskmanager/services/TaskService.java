package com.example.taskmanager.services;

import com.example.taskmanager.dto.request.TaskRequest;
import com.example.taskmanager.dto.response.TaskResponse;

import java.util.List;

public interface TaskService {
    TaskResponse addTask(TaskRequest taskRequest);

    List<TaskResponse> getaAllTasks(String status, List<Long> categoryIds, String sortOrder);

    TaskResponse getTaskById(Long id);

    TaskResponse updateTask(Long id, TaskRequest taskRequest);

    void deleteTask(Long id);

}
