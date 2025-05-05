package com.example.taskmanager.services;

import com.example.taskmanager.dto.request.LoginRequest;
import com.example.taskmanager.dto.request.RegisterRequest;
import com.example.taskmanager.dto.response.AuthResponse;
import com.example.taskmanager.dto.response.UserResponse;
import com.example.taskmanager.entities.User;

import java.util.List;

public interface UserService {
    UserResponse registerUser(RegisterRequest registerRequest);

    AuthResponse loginUser(LoginRequest loginRequest);

    List<UserResponse> getAllUsers();

    UserResponse getUserByUsername(String username);

    void verifyEmail(String token);

    void resendVerification(String email);

    User getCurrentUser();
}
