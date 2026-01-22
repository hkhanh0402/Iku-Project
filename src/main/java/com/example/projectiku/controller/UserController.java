package com.example.projectiku.controller;

import com.example.projectiku.dto.UserRequest;
import com.example.projectiku.dto.UserResponse;
import com.example.projectiku.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping
    public ResponseEntity<List<UserResponse>> findAll() {
        return ResponseEntity.status(HttpStatus.OK).body(userService.findAll());
    }

    @GetMapping("{id}")
    public ResponseEntity<UserResponse> findById(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.findById(id));
    }

    @PostMapping
    public ResponseEntity<UserResponse> add(@Valid @RequestBody UserRequest userRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.add(userRequest));
    }

    @PutMapping("{id}")
    public ResponseEntity<UserResponse> update(@Valid @PathVariable Long id, @RequestBody UserRequest userRequest) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.update(userRequest, id));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<UserResponse> delete(@PathVariable Long id) {
        userService.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
