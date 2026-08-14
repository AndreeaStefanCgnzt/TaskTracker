package com.example.learning_springboot.firstapp.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.learning_springboot.firstapp.entity.Task;
import com.example.learning_springboot.firstapp.enums.Status;
import com.example.learning_springboot.firstapp.repository.TaskRepository;

@Service
public class TaskService {
    private final TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository){
        this.taskRepository = taskRepository;
    }

    public Task createTask(Task task){
        if(taskRepository.existsByTitle(task.getTitle())){
            throw new RuntimeException("Task title already exists!");
        }

        return taskRepository.save(task);
    }

    public Task getTaskById(Long id){
        return taskRepository.findById(id).orElseThrow(() -> new RuntimeException("Task not found!"));
    }

    public List<Task> getTasksByStatus(String status){
        try {
            Status enumStatus = Status.valueOf(status.toUpperCase());
            return taskRepository.findByStatus(enumStatus);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid status");
        }
    }

    public List<Task> getAllTasks(){
        return taskRepository.findAll();
    }

    public List<Task> getTasksByAssignedUserId(Long userId){
        return taskRepository.findTaskByAssignedUserId(userId).orElseThrow(() -> new RuntimeException("Tasks not found!"));
    }

    public List<Task> getTasksByProjectId(Long projectId){
        return taskRepository.findTaskByProjectId(projectId).orElseThrow(() -> new RuntimeException("Tasks not found!"));
    }

    public Task updateTask(Long id, Task updatedTask){
        Task currentTask = getTaskById(id);

        currentTask.setTitle(updatedTask.getTitle());
        currentTask.setDescription(updatedTask.getDescription());
        currentTask.setStatus(updatedTask.getStatus());

        return taskRepository.save(currentTask);
    }

    public void deleteTask(Long id){
        taskRepository.deleteById(getTaskById(id).getId());
    }
}
