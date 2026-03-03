package com.example.projectiku.controller;

import com.example.projectiku.dto.ApiResponse;
import com.example.projectiku.dto.TaskRequest;
import com.example.projectiku.dto.TaskResponse;
import com.example.projectiku.service.TaskService;
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
public class TaskController {

    private final TaskService taskService;

    @PreAuthorize("hasRole('MANAGER')")
    @GetMapping
    public ResponseEntity<ApiResponse<List<TaskResponse>>> findAll() {
        return ResponseEntity.ok(
                new ApiResponse<>(200, "Get all tasks successfully",
                        taskService.findAll())
        );
    }

    @PreAuthorize("hasRole('MANAGER')")
    @GetMapping("{id}")
    public ResponseEntity<ApiResponse<TaskResponse>> findById(@PathVariable Long id) {
        return ResponseEntity.ok(
                new ApiResponse<>(200, "Get task successfully",
                        taskService.findById(id))
        );
    }

    @PreAuthorize("hasRole('MANAGER')")
    @PostMapping
    public ResponseEntity<ApiResponse<TaskResponse>> add(
            @Valid @RequestBody TaskRequest taskRequest) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>(201, "Task created successfully",
                        taskService.add(taskRequest)));
    }

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

    @PreAuthorize("hasRole('MANAGER')")
    @DeleteMapping("{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {

        taskService.delete(id);

        return ResponseEntity.ok(
                new ApiResponse<>(200, "Task deleted successfully", null)
        );
    }

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

    @PreAuthorize("hasRole('MANAGER')")
    @GetMapping("/project/{projectId}")
    public ResponseEntity<ApiResponse<List<TaskResponse>>> findByProjectId(
            @PathVariable Long projectId) {

        return ResponseEntity.ok(
                new ApiResponse<>(200, "Get tasks by project successfully",
                        taskService.findByProjectId(projectId))
        );
    }

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
