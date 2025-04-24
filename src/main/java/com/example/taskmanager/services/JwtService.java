package com.example.taskmanager.services;

import org.springframework.security.core.userdetails.UserDetails;

import java.util.Date;

public interface JwtService {
    String generateAccessToken(UserDetails userDetails);
    String generateRefreshToken(UserDetails userDetails);
    boolean validateAccessToken(String token);
    String extractUsername(String token);
    Date extractExpiration(String token);
    boolean isTokenExpired(String token);
}

