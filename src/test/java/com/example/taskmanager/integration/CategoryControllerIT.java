package com.example.taskmanager.integration;

import com.example.taskmanager.dto.request.CategoryRequest;
import com.example.taskmanager.dto.response.CategoryResponse;
import com.example.taskmanager.repositories.CategoryRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.core.ParameterizedTypeReference;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CategoryControllerIT {

    @Autowired
    private TestRestTemplate restTemplate;

    private CategoryRepository categoryRepository;

    @Test
    void testAddCategory() {
        CategoryRequest categoryRequest = new CategoryRequest();
        categoryRequest.setName("New Category");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<CategoryRequest> requestEntity = new HttpEntity<>(categoryRequest, headers);

        ResponseEntity<CategoryResponse> response = restTemplate
                .exchange("/category/add", HttpMethod.POST, requestEntity, CategoryResponse.class);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("New Category", response.getBody().getName());
    }

    @Test
    void testGetAllCategories() {
        ResponseEntity<List<CategoryResponse>> response = restTemplate.exchange(
                "/category/all",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<CategoryResponse>>() {}
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().size() > 0, "Categories list should not be empty");
    }

}
