package com.example.learning_springboot.firstapp.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.learning_springboot.firstapp.dto.AuthResponse;
import com.example.learning_springboot.firstapp.dto.LoginRequest;
import com.example.learning_springboot.firstapp.dto.RegisterRequest;
import com.example.learning_springboot.firstapp.entity.User;
import com.example.learning_springboot.firstapp.enums.UserRole;
import com.example.learning_springboot.firstapp.repository.UserRepository;
import com.example.learning_springboot.firstapp.security.JwtService;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthService authService;

    @Test
    public void shouldRegisterUser_andReturnToken() {
        RegisterRequest request = new RegisterRequest();
        request.setUsername("useradmintest");
        request.setEmail("useradmintest@gmail.com");
        request.setPassword("password123");
        request.setRole(UserRole.ADMIN);

        when(passwordEncoder.encode("password123")).thenReturn("encrryptedPassword");
        when(jwtService.generateToken(any(User.class))).thenReturn("token123");

        AuthResponse response = authService.register(request);

        assertNotNull(response);
        assertEquals("token123", response.getToken());

        verify(userRepository).save(any(User.class));
    }

    @Test
    public void shouldLoginUser_andGenerateToken() {
        LoginRequest request = new LoginRequest();
        request.setEmail("useradmintest@gmail.com");
        request.setPassword("password123");

        User user = new User();
        user.setEmail("useradmintest@gmail.com");

        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.of(user));
        when(jwtService.generateToken(user)).thenReturn("token123");

        AuthResponse response = authService.login(request);

        assertNotNull(response);
        assertEquals("token123", response.getToken());

        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
    }
}
