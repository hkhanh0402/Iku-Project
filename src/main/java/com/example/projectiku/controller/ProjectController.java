package com.example.projectiku.controller;

import com.example.projectiku.dto.ApiResponse;
import com.example.projectiku.dto.ProjectRequest;
import com.example.projectiku.dto.ProjectResponse;
import com.example.projectiku.service.ProjectService;
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
public class ProjectController {

    private final ProjectService projectService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<ProjectResponse>>> findAll() {
        return ResponseEntity.ok(
                new ApiResponse<>(200, "Get all projects successfully",
                        projectService.findAll())
        );
    }

    @GetMapping("{id}")
    public ResponseEntity<ApiResponse<ProjectResponse>> findById(@PathVariable Long id) {
        return ResponseEntity.ok(
                new ApiResponse<>(200, "Get project successfully",
                        projectService.findById(id))
        );
    }

    @PreAuthorize("hasRole('MANAGER')")
    @PostMapping
    public ResponseEntity<ApiResponse<ProjectResponse>> add(
            @Valid @RequestBody ProjectRequest projectRequest) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>(201, "Project created successfully",
                        projectService.add(projectRequest)));
    }

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

    @PreAuthorize("hasRole('MANAGER')")
    @DeleteMapping("{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {

        projectService.delete(id);

        return ResponseEntity.ok(
                new ApiResponse<>(200, "Project deleted successfully", null)
        );
    }

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