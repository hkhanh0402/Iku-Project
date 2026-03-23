package com.example.projectiku.controller;

import com.example.projectiku.dto.ApiResponse;
import com.example.projectiku.dto.ProjectRequest;
import com.example.projectiku.dto.ProjectResponse;
import com.example.projectiku.service.ProjectService;
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
@RequestMapping("/api/projects")
@RequiredArgsConstructor
@Tag(name = "2. Project Management", description = "API Quản lý Dự án")
@SecurityRequirement(name = "bearerAuth") // Yêu cầu nhập token trên Swagger
public class ProjectController {

    private final ProjectService projectService;

    @Operation(summary = "Lấy danh sách dự án", description = "Trả về toàn bộ danh sách dự án có trong hệ thống")
    @GetMapping
    public ResponseEntity<ApiResponse<List<ProjectResponse>>> findAll() {
        return ResponseEntity.ok(
                new ApiResponse<>(200, "Get all projects successfully",
                        projectService.findAll())
        );
    }

    @Operation(summary = "Xem chi tiết dự án", description = "Lấy thông tin của một dự án dựa vào ID")
    @GetMapping("{id}")
    public ResponseEntity<ApiResponse<ProjectResponse>> findById(@PathVariable Long id) {
        return ResponseEntity.ok(
                new ApiResponse<>(200, "Get project successfully",
                        projectService.findById(id))
        );
    }

    @Operation(summary = "Tạo mới dự án", description = "Yêu cầu quyền MANAGER")
    @PreAuthorize("hasRole('MANAGER')")
    @PostMapping
    public ResponseEntity<ApiResponse<ProjectResponse>> add(
            @Valid @RequestBody ProjectRequest projectRequest) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>(201, "Project created successfully",
                        projectService.add(projectRequest)));
    }

    @Operation(summary = "Cập nhật dự án", description = "Yêu cầu quyền MANAGER")
    @PreAuthorize("hasRole('MANAGER')")
    @PutMapping("{id}")
    public ResponseEntity<ApiResponse<ProjectResponse>> update(
            @Valid @RequestBody ProjectRequest projectRequest,
            @PathVariable Long id) {

        return ResponseEntity.ok(
                new ApiResponse<>(200, "Project updated successfully",
                        projectService.update(projectRequest, id))
        );
    }

    @Operation(summary = "Xóa dự án", description = "Yêu cầu quyền MANAGER")
    @PreAuthorize("hasRole('MANAGER')")
    @DeleteMapping("{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {

        projectService.delete(id);

        return ResponseEntity.ok(
                new ApiResponse<>(200, "Project deleted successfully", null)
        );
    }

    @Operation(summary = "Thêm thành viên vào dự án", description = "Yêu cầu quyền MANAGER")
    @PreAuthorize("hasRole('MANAGER')")
    @PostMapping("/{projectId}/users/{userId}")
    public ResponseEntity<ApiResponse<Void>> addUserToProject(
            @PathVariable Long projectId,
            @PathVariable Long userId) {

        projectService.addUserToProject(projectId, userId);

        return ResponseEntity.ok(
                new ApiResponse<>(200, "User added to project successfully", null)
        );
    }
}