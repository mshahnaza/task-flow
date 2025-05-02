package com.example.taskmanager.services;


import com.example.taskmanager.entities.User;

public interface EmailService {
    void sendSimpleEmail(String to, String subject, String body);

    void sendVerificationEmail(User user);
}
