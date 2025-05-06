package com.example.taskmanager.repositories;

import com.example.taskmanager.dto.request.CategoryRequest;
import com.example.taskmanager.dto.response.CategoryResponse;
import com.example.taskmanager.entities.Comment;
import com.example.taskmanager.entities.Task;
import com.example.taskmanager.entities.User;
import com.example.taskmanager.enums.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
public class TestCommentRepository {
    private User user;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    public void init() {
        user = User.builder()
                .username("user")
                .email("user@example.com")
                .password("user")
                .emailVerified(true)
                .roles(List.of(Role.ROLE_USER))
                .build();
        userRepository.save(user);
    }

    @Test
    public void CountByTask_ReturnCorrectCount() {
        Task task = new Task();
        task.setTitle("Test Task");
        task.setUser(user);
        taskRepository.save(task);

        Comment comment1 = new Comment();
        comment1.setContent("First Comment");
        comment1.setTask(task);
        comment1.setUser(user);
        commentRepository.save(comment1);

        Comment comment2 = new Comment();
        comment2.setContent("Second Comment");
        comment2.setTask(task);
        comment2.setUser(user);
        commentRepository.save(comment2);

        int commentCount = commentRepository.countByTask(task);

        assertThat(commentCount).isEqualTo(2);
    }

    @Test
    public void FindByTaskId_ReturnComment() {
        Task task = new Task();
        task.setTitle("Test Task");
        task.setUser(user);
        taskRepository.save(task);

        Comment comment1 = new Comment();
        comment1.setContent("First Comment");
        comment1.setTask(task);
        comment1.setUser(user);
        commentRepository.save(comment1);

        Comment comment2 = new Comment();
        comment2.setContent("Second Comment");
        comment2.setTask(task);
        comment2.setUser(user);
        commentRepository.save(comment2);

        List<Comment> comments = commentRepository.findByTaskId(task.getId());

        assertThat(comments).isNotNull();
        assertThat(comments.size()).isEqualTo(2);
    }
}
