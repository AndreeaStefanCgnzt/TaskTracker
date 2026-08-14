package com.example.learning_springboot.firstapp.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.learning_springboot.firstapp.entity.User;
import com.example.learning_springboot.firstapp.service.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService){
        this.userService = userService;
    }
    
    @PostMapping
    public ResponseEntity<User> createUser(@Valid @RequestBody User user){
        return ResponseEntity.ok(userService.createUser(user));
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable long id){
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers(){
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User user){
        return ResponseEntity.ok(userService.updateUser(id, user));
    }

    @PostMapping("/{userId}/tasks/{taskId}")
    public ResponseEntity<User> assignTaskToUser(@PathVariable Long userId, @PathVariable Long taskId){
        return ResponseEntity.ok(userService.assignTaskToUser(userId, taskId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id){
        userService.deleteUser(id);
        return ResponseEntity.ok("User with ID " + id + " deleted from database.");
    }
 }