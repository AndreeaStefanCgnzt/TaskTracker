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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.learning_springboot.firstapp.entity.Project;
import com.example.learning_springboot.firstapp.entity.User;
import com.example.learning_springboot.firstapp.service.ProjectService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/projects")
public class ProjectController {
    
    private final ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @PostMapping
    public ResponseEntity<Project> createProject(
            @AuthenticationPrincipal User currentUser,
            @Valid @RequestBody Project project) {
        
        return ResponseEntity.ok(projectService.createProject(project));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Project> getProjectById(@PathVariable Long id) {
        return ResponseEntity.ok(projectService.getProjectById(id));
    }

    @GetMapping
    public ResponseEntity<List<Project>> getAllProjects(@RequestParam(required = false) String status) {
        if (status != null) {
            return ResponseEntity.ok(projectService.getPojectsByStatus(status));
        }

        return ResponseEntity.ok(projectService.getAllProjects());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Project> updateProject(
            @AuthenticationPrincipal User currentUser,
            @PathVariable Long id, 
            @Valid @RequestBody Project project) {
        
        return ResponseEntity.ok(projectService.updateProject(id, project));
    }

    @PostMapping("/{projectId}/users/{userId}")
    public ResponseEntity<Project> assignUserToProject(
            @AuthenticationPrincipal User currentUser,
            @PathVariable Long projectId, 
            @PathVariable Long userId) {
        
        return ResponseEntity.ok(projectService.assignUserToProject(projectId, userId));
    }

    @PostMapping("/{projectId}/tasks/{taskId}")
    public ResponseEntity<Project> assignTaskToProject(
            @AuthenticationPrincipal User currentUser,
            @PathVariable Long projectId, 
            @PathVariable Long taskId) {
        
        return ResponseEntity.ok(projectService.assignTaskToProject(projectId, taskId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProject(
            @AuthenticationPrincipal User currentUser,
            @PathVariable Long id) {
        
        projectService.deleteProject(id);
        return ResponseEntity.ok("Project with ID " + id + " deleted from database.");
    }
}
