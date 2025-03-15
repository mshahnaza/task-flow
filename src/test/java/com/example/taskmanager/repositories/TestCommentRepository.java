package com.example.taskmanager.repositories;

import com.example.taskmanager.entities.Comment;
import com.example.taskmanager.entities.Task;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
public class TestCommentRepository {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private TaskRepository taskRepository;

    @Test
    public void CountByTask_ReturnCorrectCount() {
        Task task = new Task();
        task.setTitle("Test Task");
        taskRepository.save(task);

        Comment comment1 = new Comment();
        comment1.setContent("First Comment");
        comment1.setTask(task);
        commentRepository.save(comment1);

        Comment comment2 = new Comment();
        comment2.setContent("Second Comment");
        comment2.setTask(task);
        commentRepository.save(comment2);

        int commentCount = commentRepository.countByTask(task);

        assertThat(commentCount).isEqualTo(2);
    }

    @Test
    public void FindByTaskId_ReturnComment() {
        Task task = new Task();
        task.setTitle("Test Task");
        taskRepository.save(task);

        Comment comment1 = new Comment();
        comment1.setContent("First Comment");
        comment1.setTask(task);
        commentRepository.save(comment1);

        Comment comment2 = new Comment();
        comment2.setContent("Second Comment");
        comment2.setTask(task);
        commentRepository.save(comment2);

        List<Comment> comments = commentRepository.findByTaskId(task.getId());

        assertThat(comments).isNotNull();
        assertThat(comments.size()).isEqualTo(2);
    }
}
