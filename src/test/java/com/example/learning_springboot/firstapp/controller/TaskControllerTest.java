package com.example.learning_springboot.firstapp.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.example.learning_springboot.firstapp.entity.User;
import com.example.learning_springboot.firstapp.enums.UserRole;
import com.example.learning_springboot.firstapp.security.JwtAuthenticationFilter;
import com.example.learning_springboot.firstapp.security.JwtService;
import com.example.learning_springboot.firstapp.security.SecurityConfig;
import com.example.learning_springboot.firstapp.service.TaskService;

@WebMvcTest(TaskController.class)
@Import({SecurityConfig.class, JwtAuthenticationFilter.class})
public class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TaskService taskService;

    @MockitoBean
    private JwtService jwtService;

    @MockitoBean
    private UserDetailsService userDetailsService;

    @MockitoBean
    private AuthenticationProvider authenticationProvider;

    @Test
    public void shouldAllowAdmin_toCreateTask() throws Exception {
        User adminUser = new User();
        adminUser.setId(1L);
        adminUser.setEmail("useradmintest@gmail.com");
        adminUser.setPassword("password123");
        adminUser.setRole(UserRole.ADMIN);

        String taskJson = """
                {
                    "title": "Test Task",
                    "description": "Test description",
                    "status": "IN_PROGRESS"
                }
                """;
        
        mockMvc.perform(post("/api/tasks")
                .with(csrf())
                .with(user(adminUser))
                .contentType(MediaType.APPLICATION_JSON)
                .content(taskJson))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldForbidEmployee_toCreateTask() throws Exception {
        User employeeUser = new User();
        employeeUser.setId(1L);
        employeeUser.setEmail("useremployeetest@gmail.com");
        employeeUser.setPassword("password123");
        employeeUser.setRole(UserRole.EMPLOYEE);

        String taskJson = """
                {
                    "title": "Test Task",
                    "description": "Test description",
                    "status": "IN_PROGRESS"
                }
                """;
        
        mockMvc.perform(post("/api/tasks")
                .with(csrf())
                .with(user(employeeUser))
                .contentType(MediaType.APPLICATION_JSON)
                .content(taskJson))
                .andExpect(status().isForbidden());
    }

    @Test
    public void shouldAllowAdmin_toUpdateTask() throws Exception {
        User adminUser = new User();
        adminUser.setId(1L);
        adminUser.setEmail("useradmintest@gmail.com");
        adminUser.setPassword("password123");
        adminUser.setRole(UserRole.ADMIN);

        String taskJson = """
                {
                    "title": "Updated Task",
                    "description": "Updated description",
                    "status": "FINISHED"
                }
                """;
        
        mockMvc.perform(put("/api/tasks/1")
                .with(csrf())
                .with(user(adminUser))
                .contentType(MediaType.APPLICATION_JSON)
                .content(taskJson))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldForbidEmployee_toUpdateTask() throws Exception {
        User employeeUser = new User();
        employeeUser.setId(2L);
        employeeUser.setEmail("useremployeetest@gmail.com");
        employeeUser.setPassword("password123");
        employeeUser.setRole(UserRole.EMPLOYEE);

        String taskJson = """
                {
                    "title": "Updated Task",
                    "description": "Updated description",
                    "status": "FINISHED"
                }
                """;
        
        mockMvc.perform(put("/api/tasks/1")
                .with(csrf())
                .with(user(employeeUser))
                .contentType(MediaType.APPLICATION_JSON)
                .content(taskJson))
                .andExpect(status().isForbidden());
    }

    @Test
    public void shouldAllowAdmin_toDeleteTask() throws Exception {
        User adminUser = new User();
        adminUser.setId(1L);
        adminUser.setEmail("useradmintest@gmail.com");
        adminUser.setPassword("password123");
        adminUser.setRole(UserRole.ADMIN);

        mockMvc.perform(delete("/api/tasks/1")
                .with(csrf())
                .with(user(adminUser)))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldForbidEmployee_toDeleteTask() throws Exception {
        User employeeUser = new User();
        employeeUser.setId(2L);
        employeeUser.setEmail("useremployeetest@gmail.com");
        employeeUser.setPassword("password123");
        employeeUser.setRole(UserRole.EMPLOYEE);

        mockMvc.perform(delete("/api/tasks/1")
                .with(csrf())
                .with(user(employeeUser)))
                .andExpect(status().isForbidden());
    }
}