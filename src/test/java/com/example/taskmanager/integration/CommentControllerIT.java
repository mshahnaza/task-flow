package com.example.taskmanager.integration;

import com.example.taskmanager.dto.request.CommentRequest;
import com.example.taskmanager.dto.response.CommentResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CommentControllerIT {

    @Autowired
    private TestRestTemplate restTemplate;

    private Long taskId;

    @BeforeEach
    public void setUp() {
        taskId = 1L;
    }

    @Test
    void testAddComment() {
        CommentRequest commentRequest = new CommentRequest();
        commentRequest.setContent("Test comment");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<CommentRequest> requestEntity = new HttpEntity<>(commentRequest, headers);

        ResponseEntity<CommentResponse> response = restTemplate
                .exchange("/comment/add/" + taskId, HttpMethod.POST, requestEntity, CommentResponse.class);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Test comment", response.getBody().getContent());
        assertEquals(taskId, response.getBody().getTask_id());
        assertNotNull(response.getBody().getCreatedAt());
    }

    @Test
    void testDeleteComment() {
        long commentId = 1L;
        ResponseEntity<String> response = restTemplate.exchange("/comment/delete/" + commentId, HttpMethod.DELETE, null, String.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Comment deleted successfully", response.getBody());
    }

    @Test
    void testGetAllComments() {

        ResponseEntity<List> response = restTemplate.exchange("/comment/all", HttpMethod.GET, null, List.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().size() > 0);  // Проверка, что есть хотя бы один комментарий
    }

    @Test
    void testGetTaskComments() {
        long taskId = 1L;

        ResponseEntity<List<CommentResponse>> response = restTemplate.exchange(
                "/comment/task/" + taskId,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<CommentResponse>>() {}
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
}
