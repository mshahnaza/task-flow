package com.example.taskmanager.controllers;

import com.example.taskmanager.dto.request.LoginRequest;
import com.example.taskmanager.dto.request.RegisterRequest;
import com.example.taskmanager.dto.response.AuthResponse;
import com.example.taskmanager.dto.response.UserResponse;
import com.example.taskmanager.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "APIs for user registration and authentication")
public class AuthController {
    private final UserService userService;

    @PostMapping("/register")
    @Operation(
            summary = "Register a new user",
            description = "Registers a new user in the system.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "User successfully registered",
                            content = @Content(schema = @Schema(implementation = UserResponse.class))),
                    @ApiResponse(responseCode = "400", description = "Incorrect data")
            }
    )
    public ResponseEntity<UserResponse> registerUser(@RequestBody RegisterRequest registerRequest) {
        return ResponseEntity.ok(userService.registerUser(registerRequest));
    }

    @PostMapping("/login")
    @Operation(
            summary = "Login user",
            description = "Authenticates an existing user.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Authentication successful",
                            content = @Content(schema = @Schema(implementation = AuthResponse.class))),
                    @ApiResponse(responseCode = "401", description = "Invalid credentials")
            }
    )
    public ResponseEntity<AuthResponse> loginUser(@RequestBody LoginRequest loginRequest) {
        return ResponseEntity.ok(userService.loginUser(loginRequest));
    }

    @GetMapping("/verify-email")
    @Operation(
            summary = "Verify email address",
            description = "Verifies the user's email address using a token.",
            parameters = {@Parameter(name = "token", description = "Email verification token", required = true)},
            responses = {
                    @ApiResponse(responseCode = "200", description = "Email successfully verified"),
                    @ApiResponse(responseCode = "400", description = "Invalid token")
            }
    )
    public ResponseEntity<String> verifyEmail(@RequestParam String token) {
        try {
            userService.verifyEmail(token);
            return ResponseEntity.ok("Email verified successfully.");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/resend-verification")
    @Operation(
            summary = "Resend verification email",
            description = "Resends the email verification link to the user's email address.",
            parameters = {@Parameter(name = "email", description = "User's email address for resending verification", required = true)},
            responses = {
                    @ApiResponse(responseCode = "200", description = "Verification email resent.")
            }
    )
    public ResponseEntity<String> resend(@RequestParam String email) {
        userService.resendVerification(email);
        return ResponseEntity.ok("Verification email resent.");
    }

    @DeleteMapping("/user/delete")
    @Operation(
            summary = "Delete current user",
            description = "Deletes the currently authenticated user.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "User deleted successfully.")
            }
    )
    public ResponseEntity<String> deleteUser() {
        userService.deleteUser(userService.getCurrentUser().getId());
        return ResponseEntity.ok("User deleted successfully.");
    }
}