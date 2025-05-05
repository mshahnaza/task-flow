package com.example.taskmanager.services.impl;

import com.example.taskmanager.dto.request.LoginRequest;
import com.example.taskmanager.dto.request.RegisterRequest;
import com.example.taskmanager.dto.response.AuthResponse;
import com.example.taskmanager.dto.response.UserResponse;
import com.example.taskmanager.entities.User;
import com.example.taskmanager.enums.Role;
import com.example.taskmanager.mappers.UserMapper;
import com.example.taskmanager.repositories.UserRepository;
import com.example.taskmanager.services.EmailService;
import com.example.taskmanager.services.JwtService;
import com.example.taskmanager.services.UserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService, UserDetailsService {

    private final UserRepository userRepository;

    private final UserMapper userMapper;

    private final BCryptPasswordEncoder passwordEncoder;

    private AuthenticationManager authenticationManager;

    private final JwtService jwtService;

    private final EmailService emailService;

    @Autowired
    @Lazy
    public void setAuthenticationManager(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Override
    public UserResponse addAdmin(RegisterRequest registerRequest) {
        User currentUser = getCurrentUser();

        if (!currentUser.hasRole(Role.ROLE_ADMIN)) {
            throw new IllegalStateException("Not authorized to add admins");
        }

        if (userRepository.findByUsername(registerRequest.getUsername()).isPresent()) {
            throw new RuntimeException("Username is already taken.");
        }
        if (userRepository.findByEmail(registerRequest.getEmail()).isPresent()) {
            throw new RuntimeException("Email is already registered.");
        }

        String encodedPassword = passwordEncoder.encode(registerRequest.getPassword());

        User user = User.builder()
                .username(registerRequest.getUsername())
                .email(registerRequest.getEmail())
                .password(encodedPassword)
                .roles(Collections.singletonList(Role.ROLE_ADMIN))
                .emailVerified(false)
                .verificationCode(generateVerificationCode())
                .verificationCodeExpiration(LocalDateTime.now().plusHours(1))
                .build();

        User savedUser = userRepository.save(user);

        emailService.sendVerificationEmail(savedUser);

        return userMapper.userToUserDto(savedUser);
    }

    @Override
    public void deleteUser(Long id) {
        User currentUser = getCurrentUser();
        if (!currentUser.hasRole(Role.ROLE_ADMIN) && !currentUser.getId().equals(id)) {
            throw new IllegalStateException("Not authorized to delete users");
        }

        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        userRepository.delete(user);
    }

    @Override
    public UserResponse registerUser(RegisterRequest registerRequest) {
        if (userRepository.findByUsername(registerRequest.getUsername()).isPresent()) {
            throw new RuntimeException("Username is already taken.");
        }
        if (userRepository.findByEmail(registerRequest.getEmail()).isPresent()) {
            throw new RuntimeException("Email is already registered.");
        }

        String encodedPassword = passwordEncoder.encode(registerRequest.getPassword());

        User user = User.builder()
                .username(registerRequest.getUsername())
                .email(registerRequest.getEmail())
                .password(encodedPassword)
                .roles(Collections.singletonList(Role.ROLE_USER))
                .emailVerified(false)
                .verificationCode(generateVerificationCode())
                .verificationCodeExpiration(LocalDateTime.now().plusHours(1))
                .build();

        User savedUser = userRepository.save(user);

        emailService.sendVerificationEmail(savedUser);

        return userMapper.userToUserDto(savedUser);
    }

    @Override
    public AuthResponse loginUser(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getIdentifier(),
                        request.getPassword()
                )
        );

        User user = userRepository.findByEmailOrUsername(request.getIdentifier(), request.getIdentifier())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        if (!user.isEmailVerified()) {
            throw new IllegalStateException("Email is not verified");
        }

        UserDetails userDetails = userMapper.userToUserDetailsDao(user);

        String accessToken = jwtService.generateAccessToken(userDetails);
        String refreshToken = jwtService.generateRefreshToken(userDetails);

        user.setRefreshToken(refreshToken);
        userRepository.save(user);

        return AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .tokenType("Bearer")
                .username(user.getUsername())
                .build();
    }

    @Override
    public List<UserResponse> getAllUsers() {
        return userMapper.userToUserDtos(userRepository.findAll());
    }

    private String generateVerificationCode() {
        return Long.toHexString(System.currentTimeMillis());
    }

    @Override
    public UserResponse getUserByUsername(String username) {
        return userMapper.userToUserDto(userRepository.findByUsername(username).get());
    }

    @Override
    public void verifyEmail(String token) {
        User user = userRepository.findByVerificationCode(token)
                .orElseThrow(() -> new RuntimeException("Invalid token"));

        if (user.getVerificationCodeExpiration().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Token expired");
        }

        user.setEmailVerified(true);
        user.setVerificationCode(null);
        user.setVerificationCodeExpiration(null);
        userRepository.save(user);
    }

    @Override
    public void resendVerification(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Email not found"));

        user.setVerificationCode(generateVerificationCode());
        userRepository.save(user);
        emailService.sendVerificationEmail(user);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> userOpt;

        // Check if email or username entered
        if (username.contains("@")) {
            userOpt = userRepository.findByEmail(username);
        } else {
            userOpt = userRepository.findByUsername(username);
        }
        return userOpt.map(userMapper::userToUserDetailsDao)
                .orElseThrow(() ->
                        new UsernameNotFoundException("User '" + username + "' not found")
                );
    }

    @Override
    public User getCurrentUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        Optional<User> userOpt;
        if (username.contains("@")) {
            userOpt = userRepository.findByEmail(username);
        } else {
            userOpt = userRepository.findByUsername(username);
        }

        return userOpt.orElseThrow(() -> new EntityNotFoundException("User not found"));
    }

}
