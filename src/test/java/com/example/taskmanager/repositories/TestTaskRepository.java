package com.example.taskmanager.repositories;

import com.example.taskmanager.entities.Category;
import com.example.taskmanager.entities.Comment;
import com.example.taskmanager.entities.Task;
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

    @Test
    public void FindTasksSortedByCommentsAsc_ReturnSortedTasks() {
        Category category = new Category();
        category.setName("Work");
        categoryRepository.save(category);

        Task task1 = new Task();
        task1.setTitle("Task 1");
        task1.setStatus("In Progress");
        task1.setCategories(Arrays.asList(category));
        taskRepository.save(task1);

        Task task2 = new Task();
        task2.setTitle("Task 2");
        task2.setStatus("Completed");
        task2.setCategories(Arrays.asList(category));
        taskRepository.save(task2);

        Comment comment = new Comment();
        comment.setContent("Comment 1");
        comment.setTask(task1);
        commentRepository.save(comment);

        //test when there's status only
        List<Task> statusSortedTasks = taskRepository.findTasksSortedByCommentsAsc("In Progress", null);

        assertThat(statusSortedTasks).isNotNull();
        assertThat(statusSortedTasks.size()).isEqualTo(1);
        assertThat(statusSortedTasks.get(0).getTitle()).isEqualTo("Task 1");

        //test when there's category only
        List<Task> categorySortedTaskes = taskRepository.findTasksSortedByCommentsAsc(null, Arrays.asList(category.getId()));

        assertThat(categorySortedTaskes).isNotNull();
        assertThat(categorySortedTaskes.size()).isEqualTo(2);
        assertThat(categorySortedTaskes.get(0).getTitle()).isEqualTo("Task 2");

        //test when there's both category and status
        List<Task> sortedTaskes = taskRepository.findTasksSortedByCommentsAsc("Completed", Arrays.asList(2L));

        assertThat(sortedTaskes).isNotNull();
        assertThat(sortedTaskes.size()).isEqualTo(0);

        //test when there's no sorting requirements
        List<Task> notSortedTaskes = taskRepository.findTasksSortedByCommentsAsc(null, null);

        assertThat(notSortedTaskes).isNotNull();
        assertThat(notSortedTaskes.size()).isEqualTo(2);
        assertThat(notSortedTaskes.get(0).getTitle()).isEqualTo("Task 2");
    }

    @Test
    public void FindTasksSortedByCommentsDesk_ReturnSortedTasks() {
        Category category = new Category();
        category.setName("Work");
        categoryRepository.save(category);

        Task task1 = new Task();
        task1.setTitle("Task 1");
        task1.setStatus("In Progress");
        task1.setCategories(Arrays.asList(category));
        taskRepository.save(task1);

        Task task2 = new Task();
        task2.setTitle("Task 2");
        task2.setStatus("Completed");
        task2.setCategories(Arrays.asList(category));
        taskRepository.save(task2);

        Comment comment = new Comment();
        comment.setContent("Comment 1");
        comment.setTask(task1);
        commentRepository.save(comment);

        //test when there's status only
        List<Task> statusSortedTasks = taskRepository.findTasksSortedByCommentsDesc("In Progress", null);

        assertThat(statusSortedTasks).isNotNull();
        assertThat(statusSortedTasks.size()).isEqualTo(1);
        assertThat(statusSortedTasks.get(0).getTitle()).isEqualTo("Task 1");

        //test when there's category only
        List<Task> categorySortedTaskes = taskRepository.findTasksSortedByCommentsDesc(null, Arrays.asList(category.getId()));

        assertThat(categorySortedTaskes).isNotNull();
        assertThat(categorySortedTaskes.size()).isEqualTo(2);
        assertThat(categorySortedTaskes.get(0).getTitle()).isEqualTo("Task 1");

        //test when there's both category and status
        List<Task> sortedTaskes = taskRepository.findTasksSortedByCommentsDesc("Completed", Arrays.asList(2L));

        assertThat(sortedTaskes).isNotNull();
        assertThat(sortedTaskes.size()).isEqualTo(0);

        //test when there's no sorting requirements
        List<Task> notSortedTaskes = taskRepository.findTasksSortedByCommentsDesc(null, null);

        assertThat(notSortedTaskes).isNotNull();
        assertThat(notSortedTaskes.size()).isEqualTo(2);
        assertThat(notSortedTaskes.get(0).getTitle()).isEqualTo("Task 1");
    }
}
