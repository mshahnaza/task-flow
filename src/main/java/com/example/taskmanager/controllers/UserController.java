package com.example.taskmanager.controllers;

import com.example.taskmanager.dto.request.RegisterRequest;
import com.example.taskmanager.dto.response.UserResponse;
import com.example.taskmanager.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
@Tag(name = "Admin User Management", description = "APIs for administrative user management")
public class UserController {
    private final UserService userService;

    @GetMapping("/users/all")
    @Operation(
            summary = "Get all users",
            description = "Retrieves a list of all users.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "List of users successfully retrieved",
                            content = @Content(schema = @Schema(implementation = UserResponse.class, type = "array")))
            }
    )
    public List<UserResponse> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/user/{username}")
    @Operation(
            summary = "Get user by username",
            description = "Retrieves a user by their username.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "User successfully retrieved",
                            content = @Content(schema = @Schema(implementation = UserResponse.class))),
                    @ApiResponse(responseCode = "404", description = "User not found")
            },
            parameters = {@Parameter(name = "username", description = "Username of the user to retrieve", required = true)}
    )
    public UserResponse getUserByUsername(@PathVariable String username) {
        return userService.getUserByUsername(username);
    }

    @PostMapping("/register")
    @Operation(
            summary = "Register a new admin user",
            description = "Registers a new admin user in the system.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Admin user successfully registered",
                            content = @Content(schema = @Schema(implementation = UserResponse.class))),
                    @ApiResponse(responseCode = "400", description = "Incorrect data")
            }
    )
    public ResponseEntity<UserResponse> registerAdmin(@RequestBody RegisterRequest registerRequest) {
        return ResponseEntity.ok(userService.addAdmin(registerRequest));
    }

    @DeleteMapping("/delete/user/{id}")
    @Operation(
            summary = "Delete user by ID",
            description = "Deletes a user by their ID.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "User successfully deleted"),
                    @ApiResponse(responseCode = "404", description = "User not found")
            },
            parameters = {@Parameter(name = "id", description = "ID of the user to delete", required = true)}
    )
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok("Deleted user with id " + id);
    }
}