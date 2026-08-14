package com.example.learning_springboot.firstapp.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.learning_springboot.firstapp.entity.Project;
import com.example.learning_springboot.firstapp.entity.Task;
import com.example.learning_springboot.firstapp.entity.User;
import com.example.learning_springboot.firstapp.repository.ProjectRepository;
import com.example.learning_springboot.firstapp.repository.TaskRepository;
import com.example.learning_springboot.firstapp.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
public class ProjectServiceTest {
    @Mock
    private ProjectRepository projectRepository;

    @Mock
    private UserRepository userRepository;

    @Mock 
    private TaskRepository taskRepository;

    @InjectMocks
    private ProjectService projectService;

    @Test
    public void shouldCreateProject_whenNameIsValid() {
        Long projectId = 1L;
        Project project = new Project();
        project.setId(projectId);
        project.setName("Test Project");
        project.setDescription("This is a test project.");

        when(projectRepository.existsByName(project.getName())).thenReturn(false);
        when(projectRepository.save(project)).thenReturn(project);

        Project result = projectService.createProject(project);
        
        assertNotNull(result);
        assertEquals(projectId, result.getId());
        assertEquals("Test Project", result.getName());
        assertEquals("This is a test project.", result.getDescription());
    }

    @Test
    public void shouldReturnProject_whenProjectExists() {
        Long projectId = 1L;
        Project project = new Project();
        project.setId(projectId);
        project.setName("Test Project");
        project.setDescription("This is a test project.");

        when(projectRepository.findById(projectId)).thenReturn(Optional.of(project));

        Project result = projectService.getProjectById(projectId);

        assertNotNull(result);
        assertEquals(projectId, result.getId());
        assertEquals("Test Project", result.getName());
        assertEquals("This is a test project.", result.getDescription());
    }

    @Test
    public void shouldUpdateProject_whenProjectExists() {
        Long projectId = 1L;
        Project existingProject = new Project();
        existingProject.setId(projectId);
        existingProject.setName("Test Project");
        existingProject.setDescription("This is a test project.");

        Project updatedProject = new Project();
        updatedProject.setName("Updated Project");
        updatedProject.setDescription("This is an updated test project.");

        when(projectRepository.findById(projectId)).thenReturn(Optional.of(existingProject));
        when(projectRepository.save(existingProject)).thenReturn(existingProject);

        Project result = projectService.updateProject(projectId, updatedProject);

        assertNotNull(result);
        assertEquals(projectId, result.getId());
        assertEquals("Updated Project", result.getName());
        assertEquals("This is an updated test project.", result.getDescription());
    }

    @Test
    public void shouldDeleteProject_whenProjectExists() {
        Long projectId = 1L;
        Project project = new Project();
        project.setId(projectId);
        project.setName("Test Project");
        project.setDescription("This is a test project.");

        when(projectRepository.findById(projectId)).thenReturn(Optional.of(project));

        projectService.deleteProject(projectId);

        verify(projectRepository).delete(project);
    }

    @Test
    public void shouldAssignUserToProject_whenUserAndProjectExist() {
        Long projectId = 1L;
        Project project = new Project();
        project.setId(projectId);
        project.setName("Test Project");
        project.setDescription("This is a test project.");

        when(projectRepository.findById(projectId)).thenReturn(Optional.of(project));
        when(projectRepository.save(project)).thenReturn(project);
        
        Long userId = 1L;
        User user = new User();
        user.setId(userId);
        user.setUsername("testuser");
        user.setEmail("testuser@gmail.com");

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        
        Project result = projectService.assignUserToProject(projectId, user.getId());

        assertNotNull(result);
        assertEquals(projectId, result.getId());
        assertEquals(1, result.getUsers().size());
        assertEquals(userId, result.getUsers().get(0).getId());
        assertEquals("testuser@gmail.com", result.getUsers().get(0).getUsername());
    }

    @Test
        public void shouldAssignTaskToProject_whenTaskAndProjectExist() {
        Long projectId = 1L;
        Project project = new Project();
        project.setId(projectId);
        project.setName("Test Project");
        project.setDescription("This is a test project.");
        
        when(projectRepository.findById(projectId)).thenReturn(Optional.of(project));
        when(projectRepository.save(project)).thenReturn(project);


        Long taskId = 1L;
        Task task = new Task();
        task.setId(taskId);
        task.setTitle("Test Task");
        task.setDescription("This is a test task.");

        when(taskRepository.findById(taskId)).thenReturn(Optional.of(task));

        Project result = projectService.assignTaskToProject(projectId, task.getId());

        assertNotNull(result);
        assertEquals(projectId, result.getId());
        assertEquals(1, result.getTasks().size());
        assertEquals(taskId, result.getTasks().get(0).getId());
        assertEquals("Test Task", result.getTasks().get(0).getTitle());
        assertEquals("This is a test task.", result.getTasks().get(0).getDescription());
    }

    @Test
    public void shouldThrowException_whenProjectNameAlreadyExists() {
        Project project = new Project();
        project.setName("Test Project");
        project.setDescription("This is a test project.");

        when(projectRepository.existsByName(project.getName())).thenReturn(true);

        try {
            projectService.createProject(project);
        } catch (RuntimeException e) {
            assertEquals("Project name already exists!", e.getMessage());
        }
    }
}
