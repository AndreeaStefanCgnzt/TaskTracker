package com.example.learning_springboot.firstapp.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.learning_springboot.firstapp.entity.User;
import com.example.learning_springboot.firstapp.repository.UserRepository;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    public User createUser(User user){
        return userRepository.save(user);
    }
    
    public User getUserById(Long id){
        return userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found!"));
    }

    public User getUserByEmail(String email){
        return userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found!"));
    }

    public List<User> getMajorUsers(){
        return userRepository.findByAgeGreaterThan(18).orElseThrow(() -> new RuntimeException("There are no major users in this database"));
    }

    public User updateUser(Long id, User updatedUser){
        User currentUser = getUserById(id);

        currentUser.setUsername(updatedUser.getUsername());
        currentUser.setEmail(updatedUser.getEmail());
        currentUser.setAge(updatedUser.getAge());

        return userRepository.save(currentUser);
    }

    public User updateUserEmail(Long id, String email){
        User currentUser = getUserById(id);
        currentUser.setEmail(email);

        return userRepository.save(currentUser);
    }

    public void deleteUser(Long id){
        if(getUserById(id) != null){
            userRepository.delete(getUserById(id));
        }
    }
}
