package com.example.taskmanager.repositories;

import com.example.taskmanager.entities.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
