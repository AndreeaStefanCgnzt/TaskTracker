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

import com.example.learning_springboot.firstapp.entity.Task;
import com.example.learning_springboot.firstapp.repository.TaskRepository;

@ExtendWith(MockitoExtension.class)
public class TaskServiceTest {
    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private TaskService taskService;

    @Test
    public void shouldCreateTask_whenTitleIsValid() {
        Task task = new Task();
        task.setTitle("Test Task");
        task.setDescription("This is a test task.");

        when(taskRepository.existsByTitle(task.getTitle())).thenReturn(false);
        when(taskRepository.save(task)).thenReturn(task);

        Task result = taskService.createTask(task);

        assertNotNull(result);
        assertEquals("Test Task", result.getTitle());
        assertEquals("This is a test task.", result.getDescription());
    }

    @Test
    public void shouldReturnTask_whenTaskExists() {
        Long taskId = 1L;
        Task task = new Task();
        task.setId(taskId);
        task.setTitle("Test Task");
        task.setDescription("This is a test task.");

        when(taskRepository.findById(taskId)).thenReturn(Optional.of(task));

        Task result = taskService.getTaskById(taskId);

        assertNotNull(result);
        assertEquals(taskId, result.getId());
        assertEquals("Test Task", result.getTitle());
        assertEquals("This is a test task.", result.getDescription());
    }

    @Test
    public void shouldUpdateTask_whenTaskExists() {
        Long taskId = 1L;
        Task existingTask = new Task();
        existingTask.setId(taskId);
        existingTask.setTitle("Test Task");
        existingTask.setDescription("This is a test task.");
        existingTask.setStatus("In Progress...");

        Task updatedTask = new Task();
        updatedTask.setTitle("Updated Task");
        updatedTask.setDescription("This is an updated task.");
        updatedTask.setStatus("Completed");

        when(taskRepository.findById(taskId)).thenReturn(Optional.of(existingTask));
        when(taskRepository.save(existingTask)).thenReturn(existingTask);

        Task result = taskService.updateTask(taskId, updatedTask);

        assertNotNull(result);
        assertEquals(taskId, result.getId());
        assertEquals("Updated Task", result.getTitle());
        assertEquals("This is an updated task.", result.getDescription());
        assertEquals("Completed", result.getStatus());
    }

    @Test
    public void shouldDeleteTask_whenTaskExists() {
        Long taskId = 1L;
        Task task = new Task();
        task.setId(taskId);
        task.setTitle("Test Task");
        task.setDescription("This is a test task.");

        when(taskRepository.findById(taskId)).thenReturn(Optional.of(task));

        taskService.deleteTask(taskId);

        verify(taskRepository).delete(task);
    }

    @Test
    public void shouldThrowException_whenTaskTitleAlreadyExists() {
        Task task = new Task();
        task.setTitle("Test Task");
        task.setDescription("This is a test task.");

        when(taskRepository.existsByTitle(task.getTitle())).thenReturn(true);

        try {
            taskService.createTask(task);
        } catch (RuntimeException e) {
            assertEquals("Task title already exists!", e.getMessage());
        }
    }

    @Test
    public void shouldThrowException_whenDescriptionIsBiggerThanMaxLength() {
        Task task = new Task();
        task.setTitle("Test Task");
        task.setDescription("Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Aenean commodo ligula eget dolor. Aenean massa. Cum sociis natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. Donec quam felis, ultricies nec, pellentesque eu, pretium quis, sem. Nulla consequat massa quis enim. Donec pede justo, fringilla vel, aliquet nec, vulputate eget, arcu. In enim justo, rhoncus ut, imperdiet a, venenatis vitae, justo. Nullam dictum felis eu pede mollis pretium. Integer tincidunt. Cras dapibus. Vivamus elementum semper nisi. Aenean vulputate eleifend tellus. Aenean leo ligula, porttitor eu,");
        
        try {
            taskService.createTask(task);
        } catch (RuntimeException e) {
            assertEquals("Description cannot exceed 500 characters", e.getMessage());
        }
    }


}
