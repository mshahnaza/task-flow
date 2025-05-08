package com.example.taskmanager.integration;

import com.example.taskmanager.dto.request.CategoryRequest;
import com.example.taskmanager.dto.request.LoginRequest;
import com.example.taskmanager.dto.response.AuthResponse;
import com.example.taskmanager.dto.response.CategoryResponse;
import com.example.taskmanager.entities.User;
import com.example.taskmanager.enums.Role;
import com.example.taskmanager.repositories.CategoryRepository;
import com.example.taskmanager.repositories.UserRepository;
import com.example.taskmanager.services.EmailService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = {
        "spring.mail.username=user@example.com",
        "spring.mail.password=user"
})
public class CategoryControllerIT {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @MockitoBean
    private EmailService emailService;

    private ResponseEntity<AuthResponse>  authResponse;

    @BeforeEach
    public void init() {
        userRepository.deleteAll();
        categoryRepository.deleteAll();

        User user = User.builder()
                .username("user")
                .email("user@example.com")
                .password(passwordEncoder.encode("user"))
                .emailVerified(true)
                .roles(List.of(Role.ROLE_USER))
                .build();
        userRepository.save(user);

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
    void testAddCategory() {
        CategoryRequest categoryRequest = new CategoryRequest();
        categoryRequest.setName("New Category");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(authResponse.getBody().getAccessToken());
        HttpEntity<CategoryRequest> requestEntity = new HttpEntity<>(categoryRequest, headers);

        ResponseEntity<CategoryResponse> response = restTemplate
                .exchange("/category/add", HttpMethod.POST, requestEntity, CategoryResponse.class);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("New Category", response.getBody().getName());
    }

    @Test
    void testGetAllCategories() {
        testAddCategory();

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authResponse.getBody().getAccessToken());

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<List<CategoryResponse>> response = restTemplate.exchange(
                "/category/all",
                HttpMethod.GET,
                entity,
                new ParameterizedTypeReference<List<CategoryResponse>>() {}
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().size() > 0, "Categories list should not be empty");
    }

}
