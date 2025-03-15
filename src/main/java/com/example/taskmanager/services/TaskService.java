package com.example.taskmanager.services;

import com.example.taskmanager.dto.request.TaskRequest;
import com.example.taskmanager.dto.response.TaskResponse;

import java.util.List;

public interface TaskService {
    void addTask(TaskRequest taskRequest);

    List<TaskResponse> getaAllTasks();

    TaskResponse getTaskById(Long id);

    void updateTask(Long id, TaskRequest taskRequest);

    void deleteTask(Long id);
}
