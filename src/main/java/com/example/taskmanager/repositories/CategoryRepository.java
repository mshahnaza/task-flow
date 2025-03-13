package com.example.taskmanager.repositories;

import com.example.taskmanager.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
