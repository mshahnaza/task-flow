package com.example.taskmanager.exceptions;

import com.example.taskmanager.dto.request.CategoryRequest;
import com.example.taskmanager.dto.request.TaskRequest;
import com.example.taskmanager.mappers.TaskMapper;
import com.example.taskmanager.repositories.CategoryRepository;
import com.example.taskmanager.repositories.CommentRepository;
import com.example.taskmanager.repositories.TaskRepository;
import com.example.taskmanager.services.impl.TaskServiceImpl;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolationException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class GlobalExceptionHandlerTest {

    @InjectMocks
    private TaskServiceImpl taskService;

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private TaskMapper taskMapper;

    @Test(expected = IllegalArgumentException.class)
    public void addTask_ShouldThrowIllegalArgumentException_WhenTaskRequestIsNull() {
        TaskRequest taskRequest = null;
        taskService.addTask(taskRequest);
    }

    @Test(expected = EntityNotFoundException.class)
    public void getTaskById_ShouldThrowEntityNotFoundException_WhenTaskNotFound() {
        Long nonExistentTaskId = 100L;
        when(taskRepository.findById(nonExistentTaskId)).thenReturn(Optional.empty());
        taskService.getTaskById(nonExistentTaskId);
    }

    @Test(expected = EntityNotFoundException.class)
    public void deleteTask_ShouldThrowEntityNotFoundException_WhenTaskNotFound() {
        Long nonExistentTaskId = 200L;
        when(taskRepository.findById(nonExistentTaskId)).thenReturn(Optional.empty());
        taskService.deleteTask(nonExistentTaskId);
    }

    @Test
    public void addTask_ShouldThrowConstraintViolationException_WhenTaskTitleIsNull() {
        TaskRequest taskRequest = TaskRequest.builder()
                .title(null)
                .status("")
                .build();

        try {
            taskService.addTask(taskRequest);
        } catch (ConstraintViolationException ex) {
            // Check that the validation message is as expected
            String message = ex.getMessage();
            assertTrue(message.contains("Title cannot be null"));
            assertTrue(message.contains("Status cannot be blank"));
        }
    }

    @Test
    public void addTask_ShouldThrowConstraintViolationException_WhenTaskTitleIsExceedLimit() {
        TaskRequest taskRequest = TaskRequest.builder()
                .title("This title exceeds the limit which is 20 characters")
                .status("")
                .build();

        try {
            taskService.addTask(taskRequest);
        } catch (ConstraintViolationException ex) {
            // Check that the validation message is as expected
            String message = ex.getMessage();
            assertTrue(message.contains("Title size cannot exceed 20 characters"));
            assertTrue(message.contains("Status cannot be blank"));
        }
    }
}