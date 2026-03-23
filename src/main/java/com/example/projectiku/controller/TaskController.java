package com.example.projectiku.controller;

import com.example.projectiku.dto.ApiResponse;
import com.example.projectiku.dto.TaskRequest;
import com.example.projectiku.dto.TaskResponse;
import com.example.projectiku.service.TaskService;
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
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
@Tag(name = "3. Task Management", description = "API Quản lý Công việc")
@SecurityRequirement(name = "bearerAuth") // Yêu cầu nhập token trên Swagger
public class TaskController {

    private final TaskService taskService;

    @Operation(summary = "Lấy danh sách công việc", description = "Yêu cầu quyền MANAGER")
    @PreAuthorize("hasRole('MANAGER')")
    @GetMapping
    public ResponseEntity<ApiResponse<List<TaskResponse>>> findAll() {
        return ResponseEntity.ok(
                new ApiResponse<>(200, "Get all tasks successfully",
                        taskService.findAll())
        );
    }

    @Operation(summary = "Xem chi tiết công việc", description = "Yêu cầu quyền MANAGER")
    @PreAuthorize("hasRole('MANAGER')")
    @GetMapping("{id}")
    public ResponseEntity<ApiResponse<TaskResponse>> findById(@PathVariable Long id) {
        return ResponseEntity.ok(
                new ApiResponse<>(200, "Get task successfully",
                        taskService.findById(id))
        );
    }

    @Operation(summary = "Tạo mới công việc", description = "Yêu cầu quyền MANAGER")
    @PreAuthorize("hasRole('MANAGER')")
    @PostMapping
    public ResponseEntity<ApiResponse<TaskResponse>> add(
            @Valid @RequestBody TaskRequest taskRequest) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>(201, "Task created successfully",
                        taskService.add(taskRequest)));
    }

    @Operation(summary = "Cập nhật công việc", description = "Yêu cầu quyền MANAGER")
    @PreAuthorize("hasRole('MANAGER')")
    @PutMapping("{id}")
    public ResponseEntity<ApiResponse<TaskResponse>> update(
            @Valid @RequestBody TaskRequest taskRequest,
            @PathVariable Long id) {

        return ResponseEntity.ok(
                new ApiResponse<>(200, "Task updated successfully",
                        taskService.update(taskRequest, id))
        );
    }

    @Operation(summary = "Xóa công việc", description = "Yêu cầu quyền MANAGER")
    @PreAuthorize("hasRole('MANAGER')")
    @DeleteMapping("{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {

        taskService.delete(id);

        return ResponseEntity.ok(
                new ApiResponse<>(200, "Task deleted successfully", null)
        );
    }

    @Operation(summary = "Tìm công việc theo User", description = "Lấy danh sách công việc của một user cụ thể. Yêu cầu quyền MANAGER")
    @PreAuthorize("hasRole('MANAGER')")
    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse<List<TaskResponse>>> findByUserId(
            @PathVariable Long userId) {

        return ResponseEntity.ok(
                new ApiResponse<>(
                        200,
                        "Get tasks by user successfully",
                        taskService.findByUserId(userId)
                )
        );
    }

    @Operation(summary = "Tìm công việc theo Project", description = "Lấy danh sách công việc thuộc một dự án. Yêu cầu quyền MANAGER")
    @PreAuthorize("hasRole('MANAGER')")
    @GetMapping("/project/{projectId}")
    public ResponseEntity<ApiResponse<List<TaskResponse>>> findByProjectId(
            @PathVariable Long projectId) {

        return ResponseEntity.ok(
                new ApiResponse<>(200, "Get tasks by project successfully",
                        taskService.findByProjectId(projectId))
        );
    }

    @Operation(summary = "Giao việc cho User", description = "Yêu cầu quyền MANAGER")
    @PreAuthorize("hasRole('MANAGER')")
    @PutMapping("/{taskId}/assign/{userId}")
    public ResponseEntity<ApiResponse<TaskResponse>> assign(
            @PathVariable Long taskId,
            @PathVariable Long userId) {

        return ResponseEntity.ok(
                new ApiResponse<>(200, "Task assigned successfully",
                        taskService.assignTask(taskId, userId))
        );
    }

    @Operation(summary = "Xem công việc của tôi", description = "Lấy danh sách task được giao cho user đang đăng nhập. Yêu cầu quyền USER")
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/me")
    public ResponseEntity<ApiResponse<List<TaskResponse>>> getMyTasks() {

        return ResponseEntity.ok(
                new ApiResponse<>(
                        200,
                        "Get my tasks successfully",
                        taskService.getMyTasks()
                )
        );
    }
}