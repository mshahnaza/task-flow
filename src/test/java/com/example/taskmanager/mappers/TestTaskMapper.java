package com.example.taskmanager.mappers;

import com.example.taskmanager.dto.response.TaskResponse;
import com.example.taskmanager.entities.Category;
import com.example.taskmanager.entities.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
public class TestTaskMapper {
    private TaskMapper taskMapper;

    @BeforeEach
    void setUp() {
        taskMapper = new TaskMapperImpl();
    }

    @Test
    void testTaskToTaskDtoWithCommentCount() {
        Category category = new Category(1L, "Test Category", null);
        Task task = new Task(1L, "Test Task", "In Progress", "description", LocalDate.now(), List.of(category), null);

        TaskResponse taskResponse = taskMapper.taskToTaskDto(task, 5);

        assertEquals(5, taskResponse.getCommentCount());
        assertEquals(task.getId(), taskResponse.getId());
        assertEquals(task.getTitle(), taskResponse.getTitle());
        assertEquals(task.getStatus(), taskResponse.getStatus());
        assertEquals(1, taskResponse.getCategories().size());
        assertEquals(category.getName(), taskResponse.getCategories().get(0));
    }
}
