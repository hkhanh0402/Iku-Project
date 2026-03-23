package com.example.projectiku.controller;

import com.example.projectiku.dto.ApiResponse;
import com.example.projectiku.dto.UserRequest;
import com.example.projectiku.dto.UserResponse;
import com.example.projectiku.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "4. User Management", description = "API Quản lý Người dùng")
@SecurityRequirement(name = "bearerAuth") // Yêu cầu nhập token trên Swagger
public class UserController {

    private final UserService userService;

    @Operation(summary = "Lấy danh sách người dùng", description = "Yêu cầu quyền MANAGER")
    @PreAuthorize("hasRole('MANAGER')")
    @GetMapping
    public ResponseEntity<ApiResponse<List<UserResponse>>> findAll() {
        return ResponseEntity.ok(
                new ApiResponse<>(200, "Get all users successfully",
                        userService.findAll())
        );
    }

    @Operation(summary = "Xem chi tiết người dùng", description = "Yêu cầu quyền MANAGER hoặc là chính User đó")
    @PreAuthorize("hasAnyRole('USER', 'MANAGER')")
    @GetMapping("{id}")
    public ResponseEntity<ApiResponse<UserResponse>> findById(@PathVariable Long id) {
        return ResponseEntity.ok(
                new ApiResponse<>(200, "Get user successfully",
                        userService.findById(id))
        );
    }

    @Operation(summary = "Thêm người dùng mới", description = "Yêu cầu quyền MANAGER")
    @PreAuthorize("hasRole('MANAGER')")
    @PostMapping
    public ResponseEntity<ApiResponse<UserResponse>> add(
            @Valid @RequestBody UserRequest userRequest) {
        System.out.println(">>> FullName từ request: " + userRequest.getFullName());

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>(201, "User created successfully",
                        userService.add(userRequest)));
    }

    @Operation(summary = "Cập nhật thông tin người dùng", description = "Yêu cầu quyền MANAGER hoặc là chính User đó")
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

    @Operation(summary = "Xóa người dùng", description = "Yêu cầu quyền MANAGER")
    @PreAuthorize("hasRole('MANAGER')")
    @DeleteMapping("{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {

        userService.delete(id);

        return ResponseEntity.ok(
                new ApiResponse<>(200, "User deleted successfully", null)
        );
    }

    @Operation(summary = "Cập nhật quyền (Role)", description = "Cấp quyền cho user. Yêu cầu quyền MANAGER")
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