package com.example.taskmanager.mappers;

import com.example.taskmanager.dto.response.CommentResponse;
import com.example.taskmanager.entities.Comment;
import com.example.taskmanager.entities.Task;
import com.example.taskmanager.entities.User;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
public class TestCommentMapper {

    private CommentMapper commentMapper;

    @Test
    void testCommentToCommentDto() {
        commentMapper = new CommentMapperImpl();

        Task task = new Task(1L, "Test Task", "In Progress", "description", LocalDate.now(), null, null, null);
        Comment comment = new Comment(1L, "Test Comment", LocalDateTime.now(), task, null);

        CommentResponse commentResponse = commentMapper.commentToCommentDto(comment);

        assertEquals(comment.getId(), commentResponse.getId());
        assertEquals(comment.getContent(), commentResponse.getContent());
        assertEquals(comment.getTask().getId(), commentResponse.getTask_id());
    }
}
