package com.example.learning_springboot.firstapp.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.learning_springboot.firstapp.entity.User;
import com.example.learning_springboot.firstapp.entity.Task;
import com.example.learning_springboot.firstapp.repository.TaskRepository;
import com.example.learning_springboot.firstapp.repository.UserRepository;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final TaskRepository taskRepository;

    public UserService(UserRepository userRepository, TaskRepository taskRepository){
        this.userRepository = userRepository;
        this.taskRepository = taskRepository;
    }

    public User createUser(User user){
        if(userRepository.existsByUsername(user.getUsername())) {
            throw new RuntimeException("Username already exists!");
        } else if(userRepository.existsByEmail(user.getEmail())) {
            throw new RuntimeException("Email already exists!");
        } 

        return userRepository.save(user);
    }
    
    public User getUserById(Long id){
        return userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found!"));
    }

    public List<User> getAllUsers(){
        return userRepository.findAll();
    }

    public User updateUser(Long id, User updatedUser){
        User currentUser = getUserById(id);

        currentUser.setUsername(updatedUser.getUsername());
        currentUser.setEmail(updatedUser.getEmail());

        return userRepository.save(currentUser);
    }

    public User assignTaskToUser(Long userId, Long taskId){
        User user = getUserById(userId);
        Task task = taskRepository.findById(taskId).orElseThrow(() -> new RuntimeException("Task not found!"));

        task.setAssignedUser(user);
        user.getTasks().add(task);

        return userRepository.save(user);
    }
    
    public void deleteUser(Long id){
        userRepository.deleteById(getUserById(id).getId());   
    }
}
