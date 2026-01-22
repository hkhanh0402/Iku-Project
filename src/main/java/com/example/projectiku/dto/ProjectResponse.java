package com.example.projectiku.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProjectResponse {
    private Long id;

    private String name;

    private String description;

    private LocalDate startDate;

    private LocalDate endDate;
}
