package com.example.projectiku.security;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.*;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.*;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
@EnableMethodSecurity
public class SecurityConfig {

    private final JwtFilter jwtFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http.csrf(csrf -> csrf.disable());

        http.sessionManagement(session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        );

        http.addFilterBefore(jwtFilter,
                UsernamePasswordAuthenticationFilter.class);

        http.authorizeHttpRequests(auth -> auth
                .requestMatchers(
                        "/v3/api-docs/**",
                        "/swagger-ui/**",
                        "/swagger-ui.html"
                ).permitAll()

                .requestMatchers("/api/auth/**").permitAll()

                .requestMatchers(HttpMethod.GET, "/api/tasks/me")
                .hasRole("USER")

                .requestMatchers("/api/tasks/**")
                .hasRole("MANAGER")

                .requestMatchers("/api/projects/**")
                .hasRole("MANAGER")

                .requestMatchers(HttpMethod.GET, "/api/projects/*")
                .hasAnyRole("USER", "MANAGER")

                .anyRequest().authenticated()
        );

        http.exceptionHandling(ex ->
                ex.accessDeniedHandler((request, response, accessDeniedException) -> {
                    response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                })
        );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}