package com.example.learning_springboot.firstapp.repository;

import org.springframework.stereotype.Repository;

import com.example.learning_springboot.firstapp.entity.User;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    Optional<User> findByEmail(String email);

    Optional<List<User>> findByAgeGreaterThan(int age);
}
