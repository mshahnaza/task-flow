package com.example.taskmanager.integration;

import com.example.taskmanager.controllers.CommentController;
import com.example.taskmanager.dto.request.CommentRequest;
import com.example.taskmanager.dto.response.CommentResponse;
import com.example.taskmanager.services.CommentService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class CommentControllerIT {

    private MockMvc mockMvc;

    @Mock
    private CommentService commentService;

    @InjectMocks
    private CommentController commentController;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        mockMvc = MockMvcBuilders.standaloneSetup(commentController).build();
    }

    @Test
    void testAddComment() throws Exception {
        CommentRequest commentRequest = new CommentRequest();
        commentRequest.setContent("Test comment");

        CommentResponse commentResponse = new CommentResponse(1L, "Test comment", null, 1L);

        when(commentService.addComment(1L, commentRequest)).thenReturn(commentResponse);

        mockMvc.perform(post("/comment/add/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(commentRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.content").value("Test comment"));
    }

    @Test
    void testDeleteComment() throws Exception {
        doNothing().when(commentService).deleteComment(1L);

        mockMvc.perform(delete("/comment/delete/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Comment deleted successfully"));
    }

    @Test
    void testGetAllComments() throws Exception {
        // Given
        CommentResponse commentResponse1 = new CommentResponse(1L, "Comment 1", null, 1L);
        CommentResponse commentResponse2 = new CommentResponse(2L, "Comment 2", null, 1L);
        List<CommentResponse> commentResponses = Arrays.asList(commentResponse1, commentResponse2);

        when(commentService.getaAllComments()).thenReturn(commentResponses);

        // When & Then
        mockMvc.perform(get("/comment/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].content").value("Comment 1"))
                .andExpect(jsonPath("$[1].content").value("Comment 2"));
    }

    @Test
    void testGetTaskComments() throws Exception {
        // Given
        CommentResponse commentResponse1 = new CommentResponse(1L, "Comment 1", null, 1L);
        CommentResponse commentResponse2 = new CommentResponse(2L, "Comment 2", null, 1L);
        List<CommentResponse> commentResponses = Arrays.asList(commentResponse1, commentResponse2);

        when(commentService.getaAllCommentsByTaskId(1L)).thenReturn(commentResponses);

        // When & Then
        mockMvc.perform(get("/comment/task/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].content").value("Comment 1"))
                .andExpect(jsonPath("$[1].content").value("Comment 2"));
    }
}
