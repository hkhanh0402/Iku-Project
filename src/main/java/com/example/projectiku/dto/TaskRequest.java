package com.example.projectiku.dto;

import com.example.projectiku.enums.TaskStatus;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TaskRequest {
    @NotBlank(message = "Task title must not be blank")
    @Size(min = 3, max = 100, message = "Task title must be between 3 and 100 characters")
    private String title;

    @Size(max = 255, message = "Task description must not exceed 255 characters")
    private String description;

    @NotNull(message = "Task status must not be null")
    private TaskStatus status;

    @FutureOrPresent(message = "Due date must be today or in the future")
    private LocalDate dueDate;

    @NotNull(message = "User id must not be null")
    private Long userId;

    @NotNull(message = "Project id must not be null")
    private Long projectId;
}


