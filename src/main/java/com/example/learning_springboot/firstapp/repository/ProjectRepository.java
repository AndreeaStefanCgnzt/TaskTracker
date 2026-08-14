package com.example.learning_springboot.firstapp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.learning_springboot.firstapp.entity.Project;
import com.example.learning_springboot.firstapp.enums.Status;

public interface ProjectRepository extends JpaRepository<Project, Long> {
    boolean existsByName(String name);

    List<Project> findByStatus(Status status);
}
