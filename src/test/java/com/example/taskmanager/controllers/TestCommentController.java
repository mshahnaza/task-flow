package com.example.taskmanager.controllers;

import com.example.taskmanager.dto.request.CommentRequest;
import com.example.taskmanager.dto.response.CommentResponse;
import com.example.taskmanager.services.CommentService;
import com.example.taskmanager.services.JwtService;
import com.example.taskmanager.services.impl.CustomOauth2UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = CommentController.class)
@ExtendWith(MockitoExtension.class)
public class TestCommentController {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CommentService commentService;

    @MockitoBean
    private JwtService jwtService;

    @MockitoBean
    private UserDetailsService userDetailsService;

    @MockitoBean
    private CustomOauth2UserService oauth2UserService;

    @Autowired
    private ObjectMapper objectMapper;

    private CommentRequest commentRequest;
    private CommentResponse commentResponse;

    @BeforeEach
    public void init() {
        commentRequest = new CommentRequest("This is a comment");
        commentResponse = new CommentResponse(1L, "This is a comment", LocalDateTime.now(), 1L);
    }

    @Test
    public void CommentController_AddComment_ReturnsCreated() throws Exception {
        given(commentService.addComment(ArgumentMatchers.anyLong(), ArgumentMatchers.any())).willReturn(commentResponse);

        ResultActions response = mockMvc.perform(post("/comment/add/1")
                        .with(SecurityMockMvcRequestPostProcessors.jwt())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(commentRequest)));

        response.andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(commentResponse.getId()))
                .andExpect(jsonPath("$.content").value(commentResponse.getContent()));
    }

    @Test
    public void CommentController_DeleteComment_ReturnsSuccessMessage() throws Exception {
        doNothing().when(commentService).deleteComment(1L);

        ResultActions response = mockMvc.perform(delete("/comment/delete/1")
                        .with(SecurityMockMvcRequestPostProcessors.jwt())
                .contentType(MediaType.APPLICATION_JSON));

        response.andExpect(status().isOk())
                .andExpect(content().string("Comment deleted successfully"));
    }

    @Test
    public void CommentController_GetAllComments_ReturnsCommentList() throws Exception {
        List<CommentResponse> commentList = Arrays.asList(commentResponse);
        given(commentService.getaAllComments()).willReturn(commentList);

        ResultActions response = mockMvc.perform(get("/comment/all")
                        .with(SecurityMockMvcRequestPostProcessors.jwt())
                .contentType(MediaType.APPLICATION_JSON));

        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(commentList.size()));
    }

    @Test
    public void CommentController_GetTaskComments_ReturnsCommentsForTask() throws Exception {
        List<CommentResponse> commentList = Arrays.asList(commentResponse);
        given(commentService.getaAllCommentsByTaskId(1L)).willReturn(commentList);

        ResultActions response = mockMvc.perform(get("/comment/task/1")
                        .with(SecurityMockMvcRequestPostProcessors.jwt())
                .contentType(MediaType.APPLICATION_JSON));

        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(commentList.size()));
    }
}
