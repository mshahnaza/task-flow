package com.example.taskmanager.controllers;

import com.example.taskmanager.dto.request.TaskRequest;
import com.example.taskmanager.dto.response.TaskResponse;
import com.example.taskmanager.entities.Category;
import com.example.taskmanager.services.JwtService;
import com.example.taskmanager.services.TaskService;
import com.example.taskmanager.services.impl.CustomOauth2UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@WebMvcTest(controllers = TaskController.class)
@ExtendWith(MockitoExtension.class)
public class TestTaskController {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TaskService taskService;

    @MockitoBean
    private JwtService jwtService;

    @MockitoBean
    private UserDetailsService userDetailsService;

    @MockitoBean
    private CustomOauth2UserService oauth2UserService;

    @Autowired
    private ObjectMapper objectMapper;

    private TaskRequest taskRequest;
    private TaskResponse taskResponse;
    private Category category;

    @BeforeEach
    public void init() {
        category = Category.builder().id(1L).name("Work").build();
        System.out.println("Category ID: " + category.getId());  // Проверка ID
        System.out.println("Category Name: " + category.getName());  // Проверка Name
        taskRequest = new TaskRequest("New Task", "Description", "IN_PROGRESS", LocalDate.now(), List.of(category.getId()));
        taskResponse = new TaskResponse(1L, "New Task", "Description", "IN_PROGRESS", LocalDate.now(), List.of(category.getName()), 1);
        System.out.println("TaskRequest: " + taskRequest);
        System.out.println("TaskResponse: " + taskResponse);
    }


    @Test
    public void TaskController_AddTask_ReturnsCreated() throws Exception {
        given(taskService.addTask(ArgumentMatchers.any())).willReturn(taskResponse);

        ResultActions response = mockMvc.perform(post("/task/add")
                        .with(SecurityMockMvcRequestPostProcessors.jwt())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(taskRequest)));

        response.andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", CoreMatchers.is(taskResponse.getId().intValue())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.title", CoreMatchers.is(taskResponse.getTitle())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description", CoreMatchers.is(taskResponse.getDescription())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status", CoreMatchers.is(taskResponse.getStatus())));
    }

    @Test
    public void TaskController_DeleteTask_ReturnsSuccessMessage() throws Exception {
        doNothing().when(taskService).deleteTask(1L);

        ResultActions response = mockMvc.perform(delete("/task/delete/1")
                        .with(SecurityMockMvcRequestPostProcessors.jwt())
                .contentType(MediaType.APPLICATION_JSON));

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("Task successfully deleted"));
    }

    @Test
    public void TaskController_UpdateTask_ReturnsUpdatedTask() throws Exception {
        when(taskService.updateTask(1L, taskRequest)).thenReturn(taskResponse);

        ResultActions response = mockMvc.perform(patch("/task/update/1")
                        .with(SecurityMockMvcRequestPostProcessors.jwt())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(taskRequest)));

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", CoreMatchers.is(taskResponse.getId().intValue())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.title", CoreMatchers.is(taskResponse.getTitle())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description", CoreMatchers.is(taskResponse.getDescription())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status", CoreMatchers.is(taskResponse.getStatus())));
    }

    @Test
    public void TaskController_GetTaskById_ReturnsTaskResponse() throws Exception {
        when(taskService.getTaskById(1L)).thenReturn(taskResponse);

        ResultActions response = mockMvc.perform(get("/task/1")
                        .with(SecurityMockMvcRequestPostProcessors.jwt())
                .contentType(MediaType.APPLICATION_JSON));

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", CoreMatchers.is(taskResponse.getId().intValue())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.title", CoreMatchers.is(taskResponse.getTitle())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description", CoreMatchers.is(taskResponse.getDescription())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status", CoreMatchers.is(taskResponse.getStatus())));
    }

    @Test
    public void TaskController_GetAllTasks_ReturnsTaskList() throws Exception {
        List<TaskResponse> taskList = Arrays.asList(taskResponse);
        when(taskService.getaAllTasks(null, null, "desc")).thenReturn(taskList);

        ResultActions response = mockMvc.perform(get("/task/all")
                        .with(SecurityMockMvcRequestPostProcessors.jwt())
                .contentType(MediaType.APPLICATION_JSON)
                .param("sortOrder", "desc"));

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.size()", CoreMatchers.is(taskList.size())));
    }
}
