package com.example.taskmanager.repositories;

import com.example.taskmanager.entities.Comment;
import com.example.taskmanager.entities.Task;
import com.example.taskmanager.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    int countByTask(Task task);

    List<Comment> findByTaskId(Long taskId);

    List<Comment> findAllByUser(User user);
}
