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
import com.example.learning_springboot.firstapp.service.ProjectService;

@WebMvcTest(ProjectController.class)
@Import({SecurityConfig.class, JwtAuthenticationFilter.class})
public class ProjectControllerTest {
    
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ProjectService projectService;

    @MockitoBean
    private JwtService jwtService;

    @MockitoBean
    private UserDetailsService userDetailsService;

    @MockitoBean
    private AuthenticationProvider authenticationProvider;

    @Test
    public void shouldAllowAdmin_toCreateProject() throws Exception {
        User adminUser = new User();
        adminUser.setId(1L);
        adminUser.setEmail("useradmintest@gmail.com");
        adminUser.setPassword("password123");
        adminUser.setRole(UserRole.ADMIN);

        String projectJson = """
                {
                    "name": "Test Project",
                    "description": "Test description"
                }
                """;
        
        mockMvc.perform(post("/api/projects")
                .with(csrf())
                .with(user(adminUser))
                .contentType(MediaType.APPLICATION_JSON)
                .content(projectJson))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldForbidEmployee_toCreateProject() throws Exception {
        User employeeUser = new User();
        employeeUser.setId(1L);
        employeeUser.setEmail("useremployeetest@gmail.com");
        employeeUser.setPassword("password123");
        employeeUser.setRole(UserRole.EMPLOYEE);

        String projectJson = """
                {
                    "name": "Test Project",
                    "description": "Test description"
                }
                """;
        
        mockMvc.perform(post("/api/projects")
                .with(csrf())
                .with(user(employeeUser))
                .contentType(MediaType.APPLICATION_JSON)
                .content(projectJson))
                .andExpect(status().isForbidden());
    }

    @Test
    public void shouldAllowAdmin_toUpdateProject() throws Exception {
        User adminUser = new User();
        adminUser.setId(1L);
        adminUser.setEmail("useradmintest@gmail.com");
        adminUser.setPassword("password123");
        adminUser.setRole(UserRole.ADMIN);

        String projectJson = """
                {
                    "name": "Updated Project",
                    "description": "Updated description"
                }
                """;
        
        mockMvc.perform(put("/api/projects/1")
                .with(csrf())
                .with(user(adminUser))
                .contentType(MediaType.APPLICATION_JSON)
                .content(projectJson))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldForbidEmployee_toUpdateProject() throws Exception {
        User employeeUser = new User();
        employeeUser.setId(1L);
        employeeUser.setEmail("useremployeetest@gmail.com");
        employeeUser.setPassword("password123");
        employeeUser.setRole(UserRole.EMPLOYEE);

        String projectJson = """
                {
                    "name": "Updated Project",
                    "description": "Updated description"
                }
                """;
        
        mockMvc.perform(put("/api/projects/1")
                .with(csrf())
                .with(user(employeeUser))
                .contentType(MediaType.APPLICATION_JSON)
                .content(projectJson))
                .andExpect(status().isForbidden());
    }

    @Test
    public void shouldAllowAdmin_toDeleteProject() throws Exception {
        User adminUser = new User();
        adminUser.setId(1L);
        adminUser.setEmail("useradmintest@gmail.com");
        adminUser.setPassword("password123");
        adminUser.setRole(UserRole.ADMIN);

        // Folosim delete(), fără .content()
        mockMvc.perform(delete("/api/projects/1")
                .with(csrf())
                .with(user(adminUser)))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldForbidEmployee_toDeleteProject() throws Exception {
        User employeeUser = new User();
        employeeUser.setId(1L);
        employeeUser.setEmail("useremployeetest@gmail.com");
        employeeUser.setPassword("password123");
        employeeUser.setRole(UserRole.EMPLOYEE);

        mockMvc.perform(delete("/api/projects/1")
                .with(csrf())
                .with(user(employeeUser)))
                .andExpect(status().isForbidden());
    }
}