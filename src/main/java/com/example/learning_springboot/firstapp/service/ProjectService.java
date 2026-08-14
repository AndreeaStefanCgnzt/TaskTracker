package com.example.learning_springboot.firstapp.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.learning_springboot.firstapp.entity.Project;
import com.example.learning_springboot.firstapp.entity.Task;
import com.example.learning_springboot.firstapp.entity.User;
import com.example.learning_springboot.firstapp.enums.Status;
import com.example.learning_springboot.firstapp.repository.ProjectRepository;
import com.example.learning_springboot.firstapp.repository.TaskRepository;
import com.example.learning_springboot.firstapp.repository.UserRepository;

@Service
public class ProjectService {
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final TaskRepository taskRepository;

    public ProjectService(ProjectRepository projectRepository, UserRepository userRepository, TaskRepository taskRepository) {
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
        this.taskRepository = taskRepository;
    }

    public Project createProject(Project project){
        if (projectRepository.existsByName(project.getName())){
            throw new RuntimeException("Project name already exists!");
        }

        return projectRepository.save(project);
    }

    public Project getProjectById(Long id){
        return projectRepository.findById(id).orElseThrow(() -> new RuntimeException("Project not found!"));
    }

    public List<Project> getPojectsByStatus(String status){
        try {
            Status enumStatus = Status.valueOf(status.toUpperCase());
            return projectRepository.findByStatus(enumStatus);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid status");
        }
    }

    public List<Project> getAllProjects(){
        return projectRepository.findAll();
    }

    public Project updateProject(Long id, Project updatedProject){
        Project currentProject = getProjectById(id);

        currentProject.setName(updatedProject.getName());
        currentProject.setDescription(updatedProject.getDescription());

        return projectRepository.save(currentProject);
    }

    public Project assignUserToProject(Long projectId, Long userId){
        Project project = getProjectById(projectId);
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found!"));

        project.getUsers().add(user);
        user.getProjects().add(project);

        return projectRepository.save(project);
    }

    public Project assignTaskToProject(Long projectId, Long taskId){
        Project project = getProjectById(projectId);
        Task task = taskRepository.findById(taskId).orElseThrow(() -> new RuntimeException("Task not found!"));

        project.getTasks().add(task);
        task.setProject(project);

        return projectRepository.save(project);
    }

    public void deleteProject(Long id){
        if(getProjectById(id) != null){
            projectRepository.delete(getProjectById(id));
        }
    }
}
