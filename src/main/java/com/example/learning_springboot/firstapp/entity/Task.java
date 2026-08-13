package com.example.learning_springboot.firstapp.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
@Table(name="tasks")
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    @NotBlank(message = "Title is required")
    private String title;

    @Column(nullable = false)
    @NotBlank(message = "Description is required")
    @Size(max = 500, message = "Description cannot exceed 500 characters")
    private String description;

    @Column(nullable = false, columnDefinition = "VARCHAR(20) DEFAULT 'In Progress...'")
    private String status;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User assignedUser;

    @ManyToOne
    @JoinColumn(name = "project_id")
    @JsonIgnore
    private Project project;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public User getAssignedUser() {
        return assignedUser;
    }

    public void setAssignedUser(User assignedUser) {
        this.assignedUser = assignedUser;
    }

    @JsonProperty("userId")
    public Long getUserId() {
        if(this.assignedUser != null){
            return this.assignedUser.getId();
        }

        return null;
    }

    @JsonProperty("userId")
    public void setUserId(Long userId) {
        if(userId != null){
            this.assignedUser = new User();
            this.assignedUser.setId(userId);
        }
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    @JsonProperty("projectId")
    public Long getProjectId() {
        if(this.project != null){
            return this.project.getId();
        }

        return null;
    }

    @JsonProperty("projectId")
    public void setProjectId(Long projectId) {
        if(projectId != null){
            this.project = new Project();
            this.project.setId(projectId);
        }
    }
}
