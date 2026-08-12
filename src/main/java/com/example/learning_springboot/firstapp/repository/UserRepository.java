package com.example.learning_springboot.firstapp.repository;

import org.springframework.stereotype.Repository;

import com.example.learning_springboot.firstapp.entity.User;

import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByUsername(String username);
    
    boolean existsByEmail(String email);
}
