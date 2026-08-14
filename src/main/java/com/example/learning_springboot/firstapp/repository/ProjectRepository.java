package com.example.learning_springboot.firstapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.learning_springboot.firstapp.entity.Project;

public interface ProjectRepository extends JpaRepository<Project, Long> {
    boolean existsByName(String name);
}
