package com.example.taskmanager.controllers;

import com.example.taskmanager.dto.request.LoginRequest;
import com.example.taskmanager.dto.request.RegisterRequest;
import com.example.taskmanager.dto.response.AuthResponse;
import com.example.taskmanager.dto.response.UserResponse;
import com.example.taskmanager.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<UserResponse> registerUser(@RequestBody RegisterRequest registerRequest) {
        return ResponseEntity.ok(userService.registerUser(registerRequest));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> loginUser(@RequestBody LoginRequest loginRequest) {
        return ResponseEntity.ok(userService.loginUser(loginRequest));
    }

    @GetMapping("/verify-email")
    public ResponseEntity<String> verifyEmail(@RequestParam String token) {
        try {
            userService.verifyEmail(token);
            return ResponseEntity.ok("Email verified successfully.");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/resend-verification")
    public ResponseEntity<String> resend(@RequestParam String email) {
        userService.resendVerification(email);
        return ResponseEntity.ok("Verification email resent.");
    }
}
