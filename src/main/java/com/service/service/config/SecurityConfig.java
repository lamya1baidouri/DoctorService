package com.service.service.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true) // Permet l'utilisation de @PreAuthorize
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorize -> authorize
                        // Autorise l'accès à toutes les ressources Swagger sans authentification
                        .requestMatchers("/swagger-ui.html", "/swagger-ui/**", "/v3/api-docs/**").permitAll()
                        .requestMatchers("/api/doctors/**").hasRole("DOCTOR") // Sécurise les API
                        .anyRequest().authenticated()
                )
                .csrf(csrf -> csrf.disable()) // Désactive la protection CSRF si nécessaire
                .httpBasic(Customizer.withDefaults()); // Utilise l'authentification HTTP Basic

        return http.build();
    }
}
