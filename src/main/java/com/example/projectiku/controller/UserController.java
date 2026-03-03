package com.example.projectiku.controller;

import com.example.projectiku.dto.ApiResponse;
import com.example.projectiku.dto.UserRequest;
import com.example.projectiku.dto.UserResponse;
import com.example.projectiku.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PreAuthorize("hasRole('MANAGER')")
    @GetMapping
    public ResponseEntity<ApiResponse<List<UserResponse>>> findAll() {
        return ResponseEntity.ok(
                new ApiResponse<>(200, "Get all users successfully",
                        userService.findAll())
        );
    }

    @PreAuthorize("hasAnyRole('USER', 'MANAGER')")
    @GetMapping("{id}")
    public ResponseEntity<ApiResponse<UserResponse>> findById(@PathVariable Long id) {
        return ResponseEntity.ok(
                new ApiResponse<>(200, "Get user successfully",
                        userService.findById(id))
        );
    }

    @PreAuthorize("hasRole('MANAGER')")
    @PostMapping
    public ResponseEntity<ApiResponse<UserResponse>> add(
            @Valid @RequestBody UserRequest userRequest) {
        System.out.println(">>> FullName từ request: " + userRequest.getFullName());

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>(201, "User created successfully",
                        userService.add(userRequest)));
    }

    @PreAuthorize("hasAnyRole('USER','MANAGER')")
    @PutMapping("{id}")
    public ResponseEntity<ApiResponse<UserResponse>> update(
            @PathVariable Long id,
            @Valid @RequestBody UserRequest userRequest) {

        return ResponseEntity.ok(
                new ApiResponse<>(200, "User updated successfully",
                        userService.update(userRequest, id))
        );
    }

    @PreAuthorize("hasRole('MANAGER')")
    @DeleteMapping("{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {

        userService.delete(id);

        return ResponseEntity.ok(
                new ApiResponse<>(200, "User deleted successfully", null)
        );
    }

    @PreAuthorize("hasRole('MANAGER')")
    @PutMapping("/{id}/roles")
    public ResponseEntity<ApiResponse<String>> updateRole(
            @PathVariable Long id,
            @RequestBody List<String> roles) {

        userService.updateRole(id, roles);

        return ResponseEntity.ok(
                new ApiResponse<>(200, "Role updated successfully", null)
        );
    }
}
