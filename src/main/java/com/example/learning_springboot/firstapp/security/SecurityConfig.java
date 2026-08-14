package com.example.learning_springboot.firstapp.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private final JwtAuthenticationFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthFilter, AuthenticationProvider authenticationProvider) {
        this.jwtAuthFilter = jwtAuthFilter;
        this.authenticationProvider = authenticationProvider;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(AbstractHttpConfigurer::disable)
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/auth/**").permitAll()

                .requestMatchers(HttpMethod.POST, "/api/projects", "/api/projects/**").hasAuthority("ADMIN")
                .requestMatchers(HttpMethod.PUT, "/api/projects/**").hasAuthority("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/api/projects/**").hasAuthority("ADMIN")
                
                .requestMatchers(HttpMethod.POST, "/api/tasks", "/api/tasks/**").hasAuthority("ADMIN")
                .requestMatchers(HttpMethod.PUT, "/api/tasks/**").hasAuthority("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/api/tasks/**").hasAuthority("ADMIN")

                .requestMatchers(HttpMethod.POST, "/api/users/*/tasks/*").hasAuthority("ADMIN")

                .anyRequest().authenticated()
            )
            .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authenticationProvider(authenticationProvider)
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}
