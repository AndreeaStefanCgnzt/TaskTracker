package com.example.learning_springboot.firstapp.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.learning_springboot.firstapp.entity.Task;
import com.example.learning_springboot.firstapp.entity.User;
import com.example.learning_springboot.firstapp.service.TaskService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {
    
    private final TaskService taskService;

    public TaskController(TaskService taskService){
        this.taskService = taskService;
    }
    
    @PostMapping
    public ResponseEntity<Task> createTask(
            @AuthenticationPrincipal User currentUser,
            @Valid @RequestBody Task task){
        
        System.out.println("Adminul " + currentUser.getEmail() + " a creat un task nou.");
        return ResponseEntity.ok(taskService.createTask(task));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Task> getTaskById(@PathVariable Long id){
        return ResponseEntity.ok(taskService.getTaskById(id));
    }

    @GetMapping
    public ResponseEntity<List<Task>> getAllTasks(){
        return ResponseEntity.ok(taskService.getAllTasks());
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Task>> getTasksByAssignedUserId(@PathVariable Long userId){
        return ResponseEntity.ok(taskService.getTasksByAssignedUserId(userId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Task> updateTask(
            @AuthenticationPrincipal User currentUser,
            @PathVariable Long id, 
            @Valid @RequestBody Task task){
        
        return ResponseEntity.ok(taskService.updateTask(id, task)); 
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteTask(
            @AuthenticationPrincipal User currentUser,
            @PathVariable Long id){
        
        taskService.deleteTask(id);
        return ResponseEntity.ok("Task with ID " + id + " deleted from database.");
    }
}
