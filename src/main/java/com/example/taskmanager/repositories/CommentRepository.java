package com.example.taskmanager.repositories;

import com.example.taskmanager.entities.Comment;
import com.example.taskmanager.entities.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    int countByTask(Task task);

    List<Comment> findByTaskId(Long taskId);
}
