package com.example.taskmanager.repositories;

import com.example.taskmanager.entities.Comment;
import com.example.taskmanager.entities.Task;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    int countByTask(Task task);
}
