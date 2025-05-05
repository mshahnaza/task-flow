package com.example.taskmanager.repositories;

import com.example.taskmanager.entities.Category;
import com.example.taskmanager.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    Optional<Category> findAllByUser(User user);
}
