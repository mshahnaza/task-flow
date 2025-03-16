package com.example.taskmanager.integration;

import com.example.taskmanager.dto.request.TaskRequest;
import com.example.taskmanager.dto.response.TaskResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TaskControllerIT {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void testAddTask() {
        TaskRequest taskRequest = new TaskRequest();
        taskRequest.setTitle("New Task");
        taskRequest.setDescription("Description of the new task");
        taskRequest.setStatus("In Progress");
        taskRequest.setDueDate(LocalDate.of(2025, 3, 17));
        taskRequest.setCategory_ids(List.of(1L, 2L));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<TaskRequest> requestEntity = new HttpEntity<>(taskRequest, headers);

        ResponseEntity<TaskResponse> response = restTemplate
                .exchange("/task/add", HttpMethod.POST, requestEntity, TaskResponse.class);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("New Task", response.getBody().getTitle());
        assertEquals("Description of the new task", response.getBody().getDescription());
        assertEquals("In Progress", response.getBody().getStatus());
        assertEquals(LocalDate.of(2025, 3, 17), response.getBody().getDueDate());
        assertNotNull(response.getBody().getCategories());
    }


    @Test
    void testGetTaskById() {
        long taskId = 1L;
        ResponseEntity<TaskResponse> response = restTemplate.getForEntity("/task/" + taskId, TaskResponse.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(taskId, response.getBody().getId());
    }

    @Test
    void testUpdateTask() {
        long taskId = 1L;
        TaskRequest updateRequest = new TaskRequest();
        updateRequest.setTitle("Updated Task");
        updateRequest.setDescription("Updated description");
        updateRequest.setStatus("Completed");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<TaskRequest> requestEntity = new HttpEntity<>(updateRequest, headers);

        ResponseEntity<TaskResponse> response = restTemplate
                .exchange("/task/update/" + taskId, HttpMethod.PATCH, requestEntity, TaskResponse.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Updated Task", response.getBody().getTitle());
    }

    @Test
    void testDeleteTask() {
        long taskId = 1L;
        ResponseEntity<String> response = restTemplate.exchange("/task/delete/" + taskId, HttpMethod.DELETE, null, String.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Task deleted successfully", response.getBody());
    }
}
