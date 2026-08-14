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
import com.example.learning_springboot.firstapp.entity.User;
import com.example.learning_springboot.firstapp.repository.TaskRepository;
import com.example.learning_springboot.firstapp.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private UserService userService;

    @Test
    public void shouldCreateUser_whenUsernameAndEmailAreValid() {
        User user = new User();
        user.setUsername("testuser");
        user.setEmail("testuser@gmail.com");

        when(userRepository.existsByUsername(user.getUsername())).thenReturn(false);
        when(userRepository.existsByEmail(user.getEmail())).thenReturn(false);
        when(userRepository.save(user)).thenReturn(user);

        User result = userService.createUser(user);

        assertNotNull(result);
        assertEquals("testuser", result.getUsername());
        assertEquals("testuser@gmail.com", result.getEmail());
    }

    @Test
    public void shouldReturnUser_whenUserExists() {
        Long userId = 1L;
        User user = new User();
        user.setId(userId);
        user.setUsername("testuser");
        user.setEmail("testuser@gmail.com");

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        User result = userService.getUserById(userId);

        assertNotNull(result);
        assertEquals(userId, result.getId());
        assertEquals("testuser", result.getUsername());
        assertEquals("testuser@gmail.com", result.getEmail());
    }

    @Test
    public void shouldUpdateUser_whenUserExists() {
        Long userId = 1L;
        User existingUser = new User();
        existingUser.setId(userId);
        existingUser.setUsername("testuser");
        existingUser.setEmail("testuser@gmail.com");

        User updatedUser = new User();
        updatedUser.setUsername("updateduser");
        updatedUser.setEmail("updateduser@gmail.com");

        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
        when(userRepository.save(existingUser)).thenReturn(existingUser);

        User result = userService.updateUser(userId, updatedUser);
        
        assertNotNull(result);
        assertEquals(userId, result.getId());
        assertEquals("updateduser", result.getUsername());
        assertEquals("updateduser@gmail.com", result.getEmail());
    }

    @Test
    public void shouldDeleteUser_whenUserExists() {
        Long userId = 1L;
        User user = new User();
        user.setId(userId);
        user.setUsername("testuser");
        user.setEmail("tesuser@gmail.com");

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        userService.deleteUser(userId);

        verify(userRepository).deleteById(userId);
    }

    @Test
    public void shouldAssignTaskToUser_whenUserExists() {
        Long userId = 1L;
        User user = new User();
        user.setId(userId);
        user.setUsername("testuser");
        user.setEmail("tesuser@gmail.com");

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userRepository.save(user)).thenReturn(user);

        Long taskId = 1L;
        Task task = new Task();
        task.setId(taskId);
        task.setTitle("Test Task");
        task.setDescription("This is a test task.");

        when(taskRepository.findById(taskId)).thenReturn(Optional.of(task));

        User result = userService.assignTaskToUser(userId, task.getId());

        assertNotNull(result);
        assertEquals(userId, result.getId());
        assertEquals(1, result.getTasks().size());
        assertEquals("Test Task", result.getTasks().get(0).getTitle());
        assertEquals("This is a test task.", result.getTasks().get(0).getDescription());
    }

    @Test
    public void shouldThrowException_whenUserDoesNotExist() {
        Long userId = 1L;

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        try {
            userService.getUserById(userId);
        } catch (RuntimeException e) {
            assertEquals("User not found!", e.getMessage());
        }
    }

    @Test
    public void shouldThrowException_whenUsernameAlreadyExists() {
        User user = new User();
        user.setUsername("testuser");
        user.setEmail("testuser@gmail.com");

        when(userRepository.existsByUsername(user.getUsername())).thenReturn(true);

        try {
            userService.createUser(user);
        } catch (RuntimeException e) {
            assertEquals("Username already exists!", e.getMessage());
        }
    }

    @Test
    public void shouldThrowException_whenEmailAlreadyExists() {
        User user = new User();
        user.setUsername("testuser");
        user.setEmail("testuser@gmail.com");

        when(userRepository.existsByUsername(user.getUsername())).thenReturn(false);
        when(userRepository.existsByEmail(user.getEmail())).thenReturn(true);

        try {
            userService.createUser(user);
        } catch (RuntimeException e) {
            assertEquals("Email already exists!", e.getMessage());
        }
    }

    @Test
    public void shouldThrowException_whenEmailIsNotValid() {
        Long userId = 1L;
        User updatedUser = new User();
        updatedUser.setUsername("updateduser");
        updatedUser.setEmail("invalid.email");
    
        when(userRepository.findById(userId)).thenReturn(Optional.of(new User()));  

        try {
            userService.updateUser(userId, updatedUser);
        } catch (RuntimeException e) {
            assertEquals("Email should be valid", e.getMessage());
        }
    }
}
