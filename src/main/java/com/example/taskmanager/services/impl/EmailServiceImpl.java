package com.example.taskmanager.services.impl;

import com.example.taskmanager.entities.User;
import com.example.taskmanager.services.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {
    private final JavaMailSender mailSender;

    @Override
    public void sendSimpleEmail(String to, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);

        mailSender.send(message);
    }

    public void sendVerificationEmail(User user) {
        String link = "http://localhost:8080/auth/verify-email?token=" + user.getVerificationCode();
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(user.getEmail());
        message.setSubject("Verification");
        message.setText(link);

        mailSender.send(message);
        System.out.println("Verification email sent to " + user.getEmail() + ": " + link);
    }
}
