package com.example.projectiku.dto;

import com.example.projectiku.enums.TaskStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TaskResponse {
    private Long id;

    private String title;

    private String description;

    private TaskStatus status;

    private LocalDate dueDate;

    private Long userId;

    private Long projectId;
}
