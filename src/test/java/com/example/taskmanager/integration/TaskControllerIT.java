package com.example.taskmanager.integration;

import com.example.taskmanager.dto.request.LoginRequest;
import com.example.taskmanager.dto.request.TaskRequest;
import com.example.taskmanager.dto.response.AuthResponse;
import com.example.taskmanager.dto.response.TaskResponse;
import com.example.taskmanager.entities.Category;
import com.example.taskmanager.entities.Task;
import com.example.taskmanager.entities.User;
import com.example.taskmanager.enums.Role;
import com.example.taskmanager.repositories.CategoryRepository;
import com.example.taskmanager.repositories.TaskRepository;
import com.example.taskmanager.repositories.UserRepository;
import com.example.taskmanager.services.EmailService;
import org.checkerframework.checker.units.qual.A;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = {
        "spring.mail.username=user@example.com",
        "spring.mail.password=user"
})
public class TaskControllerIT {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    @Autowired
    private EmailService emailService;

    private ResponseEntity<AuthResponse> authResponse;
    private User user;
    private Category work;
    private Category personal;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
        categoryRepository.deleteAll();

        user = User.builder()
                .username("user")
                .email("user@example.com")
                .password(passwordEncoder.encode("user"))
                .emailVerified(true)
                .roles(List.of(Role.ROLE_USER))
                .build();
        userRepository.save(user);

        work = Category.builder().name("Work").user(user).build();
        personal = Category.builder().name("Personal").user(user).build();

        categoryRepository.saveAll(List.of(work, personal));

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
    void testAddTask() {
        TaskRequest taskRequest = new TaskRequest();
        taskRequest.setTitle("New Task");
        taskRequest.setDescription("Description of the new task");
        taskRequest.setStatus("In Progress");
        taskRequest.setDueDate(LocalDate.of(2025, 3, 17));
        taskRequest.setCategory_ids(List.of(work.getId(), personal.getId()));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(authResponse.getBody().getAccessToken());
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
        Task task = Task.builder()
                .title("Complete project")
                .description("Finish the Spring Boot backend")
                .status("In Progress")
                .dueDate(LocalDate.now().plusDays(5))
                .categories(List.of(work))
                .user(user)
                .build();

        taskRepository.save(task);

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authResponse.getBody().getAccessToken());

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<TaskResponse> response = restTemplate.exchange("/task/" + task.getId(),HttpMethod.GET,entity, TaskResponse.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(task.getId(), response.getBody().getId());
    }

    @Test
    void testUpdateTask() {
        Task task = Task.builder()
                .title("Complete project")
                .description("Finish the Spring Boot backend")
                .status("In Progress")
                .dueDate(LocalDate.now().plusDays(5))
                .categories(List.of(work))
                .user(user)
                .build();

        taskRepository.save(task);

        TaskRequest updateRequest = new TaskRequest();
        updateRequest.setTitle("Updated Task");
        updateRequest.setDescription("Updated description");
        updateRequest.setStatus("Completed");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(authResponse.getBody().getAccessToken());
        HttpEntity<TaskRequest> requestEntity = new HttpEntity<>(updateRequest, headers);

        ResponseEntity<TaskResponse> response = restTemplate
                .exchange("/task/update/" + task.getId(), HttpMethod.PATCH, requestEntity, TaskResponse.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Updated Task", response.getBody().getTitle());
    }

    @Test
    void testDeleteTask() {
        Task task = Task.builder()
                .title("Complete project")
                .description("Finish the Spring Boot backend")
                .status("In Progress")
                .dueDate(LocalDate.now().plusDays(5))
                .categories(List.of(work))
                .user(user)
                .build();

        taskRepository.save(task);

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authResponse.getBody().getAccessToken());

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange("/task/delete/" + task.getId(), HttpMethod.DELETE, entity, String.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Task successfully deleted", response.getBody());
    }
}
