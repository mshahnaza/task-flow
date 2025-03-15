package com.example.taskmanager.repositories;

import com.example.taskmanager.entities.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    @Query("SELECT t FROM Task t LEFT JOIN t.comments c LEFT JOIN t.categories cat " +
            "WHERE (:status IS NULL OR t.status = :status) " +
            "AND (:categoryIds IS NULL OR cat.id IN :categoryIds) " +
            "GROUP BY t.id " +
            "ORDER BY SIZE(t.comments) ASC")
    List<Task> findTasksSortedByCommentsAsc(@Param("status") String status,
                                            @Param("categoryIds") List<Long> categoryIds);

    @Query("SELECT t FROM Task t LEFT JOIN t.comments c LEFT JOIN t.categories cat " +
            "WHERE (:status IS NULL OR t.status = :status) " +
            "AND (:categoryIds IS NULL OR cat.id IN :categoryIds) " +
            "GROUP BY t.id " +
            "ORDER BY SIZE(t.comments) DESC")
    List<Task> findTasksSortedByCommentsDesc(@Param("status") String status,
                                             @Param("categoryIds") List<Long> categoryIds);
}
