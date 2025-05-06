package com.example.taskmanager.repositories;

import com.example.taskmanager.entities.Category;
import com.example.taskmanager.entities.Comment;
import com.example.taskmanager.entities.Task;
import com.example.taskmanager.entities.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
public class TestTaskRepository {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private UserRepository userRepository;

    @Test
    public void FindTasksSortedByCommentsAsc_ReturnSortedTasks() {
        User user1 = User.builder()
                .username("user1")
                .email("user1@example.com")
                .password("user")
                .emailVerified(true)
                .build();
        userRepository.save(user1);

        User user2 = User.builder()
                .username("user2")
                .email("user2@example.com")
                .password("user")
                .emailVerified(true)
                .build();
        userRepository.save(user2);

        Category category = new Category();
        category.setName("Work");
        category.setUser(user1);
        categoryRepository.save(category);

        Task task1 = new Task();
        task1.setTitle("Task 1");
        task1.setStatus("In Progress");
        task1.setCategories(Arrays.asList(category));
        task1.setUser(user1);
        taskRepository.save(task1);

        Task task2 = new Task();
        task2.setTitle("Task 2");
        task2.setStatus("Completed");
        task2.setCategories(Arrays.asList(category));
        task2.setUser(user2);
        taskRepository.save(task2);

        Comment comment = new Comment();
        comment.setContent("Comment 1");
        comment.setTask(task1);
        comment.setUser(user1);
        commentRepository.save(comment);

        //test when there's status only
        List<Task> statusSortedTasks = taskRepository.findTasksSortedByCommentsAsc("In Progress", null, user1);

        assertThat(statusSortedTasks).isNotNull();
        assertThat(statusSortedTasks.size()).isEqualTo(1);
        assertThat(statusSortedTasks.get(0).getTitle()).isEqualTo("Task 1");

        //test when there's category only
        List<Task> categorySortedTaskes = taskRepository.findTasksSortedByCommentsAsc(null, Arrays.asList(category.getId()), user2);

        assertThat(categorySortedTaskes).isNotNull();
        assertThat(categorySortedTaskes.size()).isEqualTo(1);
        assertThat(categorySortedTaskes.get(0).getTitle()).isEqualTo("Task 2");

        //test when there's both category, status
        List<Task> sortedTaskes = taskRepository.findTasksSortedByCommentsAsc("Completed", Arrays.asList(1L), user1);

        assertThat(sortedTaskes).isNotNull();
        assertThat(sortedTaskes.size()).isEqualTo(0);

        //test when there's no sorting requirements
        List<Task> notSortedTaskes = taskRepository.findTasksSortedByCommentsAsc(null, null, user1);

        assertThat(notSortedTaskes).isNotNull();
        assertThat(notSortedTaskes.size()).isEqualTo(1);
        assertThat(notSortedTaskes.get(0).getTitle()).isEqualTo("Task 1");
    }

    @Test
    public void FindTasksSortedByCommentsDesk_ReturnSortedTasks() {
        User user1 = User.builder()
                .username("user1")
                .email("user1@example.com")
                .password("user")
                .emailVerified(true)
                .build();
        userRepository.save(user1);

        User user2 = User.builder()
                .username("user2")
                .email("user2@example.com")
                .password("user")
                .emailVerified(true)
                .build();
        userRepository.save(user2);

        Category category = new Category();
        category.setName("Work");
        category.setUser(user1);
        categoryRepository.save(category);

        Task task1 = new Task();
        task1.setTitle("Task 1");
        task1.setStatus("In Progress");
        task1.setCategories(Arrays.asList(category));
        task1.setUser(user1);
        taskRepository.save(task1);

        Task task2 = new Task();
        task2.setTitle("Task 2");
        task2.setStatus("Completed");
        task2.setCategories(Arrays.asList(category));
        task2.setUser(user2);
        taskRepository.save(task2);

        Comment comment = new Comment();
        comment.setContent("Comment 1");
        comment.setTask(task1);
        comment.setUser(user1);
        commentRepository.save(comment);

        //test when there's status only
        List<Task> statusSortedTasks = taskRepository.findTasksSortedByCommentsDesc("In Progress", null, user1);

        assertThat(statusSortedTasks).isNotNull();
        assertThat(statusSortedTasks.size()).isEqualTo(1);
        assertThat(statusSortedTasks.get(0).getTitle()).isEqualTo("Task 1");

        //test when there's category only
        List<Task> categorySortedTaskes = taskRepository.findTasksSortedByCommentsDesc(null, Arrays.asList(category.getId()), user2);

        assertThat(categorySortedTaskes).isNotNull();
        assertThat(categorySortedTaskes.size()).isEqualTo(1);
        assertThat(categorySortedTaskes.get(0).getTitle()).isEqualTo("Task 2");

        //test when there's both category, status
        List<Task> sortedTaskes = taskRepository.findTasksSortedByCommentsDesc("Completed", Arrays.asList(1L), user2);

        assertThat(sortedTaskes).isNotNull();
        assertThat(sortedTaskes.size()).isEqualTo(0);

        //test when there's no sorting requirements
        List<Task> notSortedTaskes = taskRepository.findTasksSortedByCommentsDesc(null, null, user2);

        assertThat(notSortedTaskes).isNotNull();
        assertThat(notSortedTaskes.size()).isEqualTo(1);
        assertThat(notSortedTaskes.get(0).getTitle()).isEqualTo("Task 2");
    }
}
