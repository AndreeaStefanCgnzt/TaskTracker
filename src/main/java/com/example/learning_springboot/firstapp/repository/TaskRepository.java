package com.example.learning_springboot.firstapp.repository;

import com.example.learning_springboot.firstapp.entity.Task;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    boolean existsByTitle(String title);
    
    Optional<List<Task>> findTaskByAssignedUserId(Long userId);
}
