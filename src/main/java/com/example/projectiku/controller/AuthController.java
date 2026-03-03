package com.example.projectiku.controller;

import com.example.projectiku.dto.*;
import com.example.projectiku.entity.User;
import com.example.projectiku.exception.CustomResourceNotFoundException;
import com.example.projectiku.repository.UserRepo;
import com.example.projectiku.security.CustomUserDetailsService;
import com.example.projectiku.security.JwtUtil;
import com.example.projectiku.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserRepo userRepo;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final CustomUserDetailsService userDetailsService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<UserResponse>> register(
            @Valid @RequestBody RegisterRequest request) {

        return ResponseEntity.ok(
                new ApiResponse<>(200,
                        "Register successfully",
                        userService.register(request))
        );
    }

    @PostMapping("/login")
    public ApiResponse<String> login(@RequestBody LoginRequest request) {

        User user = userRepo.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("Invalid username"));

        if (!passwordEncoder.matches(request.getPassword(),
                user.getPassword())) {
            throw new CustomResourceNotFoundException("Invalid credentials");
        }

        UserDetails userDetails =
                userDetailsService.loadUserByUsername(user.getUsername());

        String token = jwtUtil.generateToken(userDetails);

        return new ApiResponse<>(200, "Login success", token);
    }
}