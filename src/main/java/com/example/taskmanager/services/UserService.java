package com.example.taskmanager.services;

import com.example.taskmanager.dto.request.LoginRequest;
import com.example.taskmanager.dto.request.RegisterRequest;
import com.example.taskmanager.dto.response.AuthResponse;
import com.example.taskmanager.dto.response.UserResponse;

public interface UserService {
    UserResponse registerUser(RegisterRequest registerRequest);

    AuthResponse loginUser(LoginRequest loginRequest);

    UserResponse getUserByUsername(String username);
}
