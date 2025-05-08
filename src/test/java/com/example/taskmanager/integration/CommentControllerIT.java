package com.example.taskmanager.integration;

import com.example.taskmanager.dto.request.CommentRequest;
import com.example.taskmanager.dto.request.LoginRequest;
import com.example.taskmanager.dto.response.AuthResponse;
import com.example.taskmanager.dto.response.CommentResponse;
import com.example.taskmanager.entities.Category;
import com.example.taskmanager.entities.Comment;
import com.example.taskmanager.entities.Task;
import com.example.taskmanager.entities.User;
import com.example.taskmanager.enums.Role;
import com.example.taskmanager.mappers.CategoryMapper;
import com.example.taskmanager.repositories.CategoryRepository;
import com.example.taskmanager.repositories.CommentRepository;
import com.example.taskmanager.repositories.TaskRepository;
import com.example.taskmanager.repositories.UserRepository;
import com.example.taskmanager.services.EmailService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = {
        "spring.mail.username=user@example.com",
        "spring.mail.password=user"
})
public class CommentControllerIT {

    @Autowired
    private TestRestTemplate restTemplate;

    private Long taskId;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CategoryMapper categoryMapper;

    @MockitoBean
    private EmailService emailService;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    private ResponseEntity<AuthResponse> authResponse;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    private User user;
    private Task task;

    @BeforeEach
    public void setUp() {

        userRepository.deleteAll();
        commentRepository.deleteAll();
        taskRepository.deleteAll();
        categoryRepository.deleteAll();

        user = User.builder()
                .username("user")
                .email("user@example.com")
                .password(passwordEncoder.encode("user"))
                .emailVerified(true)
                .roles(List.of(Role.ROLE_USER))
                .build();
        userRepository.save(user);

        Category work = Category.builder().name("Work").user(user).build();

        categoryRepository.save(work);

        task = Task.builder()
                .title("Complete project")
                .description("Finish the Spring Boot backend")
                .status("In Progress")
                .dueDate(LocalDate.now().plusDays(5))
                .categories(List.of(work))
                .user(user)
                .build();

        taskRepository.save(task);

        taskId = task.getId();

        LoginRequest loginRequest = LoginRequest.builder()
                .identifier("user")
                .password("user")
                .build();

        HttpHeaders loginHeaders = new HttpHeaders();
        loginHeaders.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<LoginRequest> loginRequestEntity = new HttpEntity<>(loginRequest, loginHeaders);

        authResponse = restTemplate
                .exchange("/auth/login", HttpMethod.POST, loginRequestEntity, AuthResponse.class);
    }

    @Test
    void testAddComment() {
        CommentRequest commentRequest = new CommentRequest();
        commentRequest.setContent("Test comment");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(authResponse.getBody().getAccessToken());
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
        Comment comment = Comment.builder()
                .content("Need to finish this by next Monday.")
                .createdAt(LocalDateTime.now())
                .task(task)
                .user(user)
                .build();
        commentRepository.save(comment);

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authResponse.getBody().getAccessToken());

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange("/comment/delete/" + comment.getId(), HttpMethod.DELETE, entity, String.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Comment deleted successfully", response.getBody());
    }

    @Test
    void testGetAllComments() {
        testAddComment();

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authResponse.getBody().getAccessToken());

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<List> response = restTemplate.exchange("/comment/all", HttpMethod.GET, entity, List.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().size() > 0);
    }

    @Test
    void testGetTaskComments() {
        Comment comment = Comment.builder()
                .content("Need to finish this by next Monday.")
                .createdAt(LocalDateTime.now())
                .task(task)
                .user(user)
                .build();
        commentRepository.save(comment);

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authResponse.getBody().getAccessToken());

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<List<CommentResponse>> response = restTemplate.exchange(
                "/comment/task/" + taskId,
                HttpMethod.GET,
                entity,
                new ParameterizedTypeReference<List<CommentResponse>>() {}
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
}
