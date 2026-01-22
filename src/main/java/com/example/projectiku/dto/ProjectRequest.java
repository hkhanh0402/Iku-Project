package com.example.projectiku.dto;

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
public class ProjectRequest {
    @NotBlank(message = "Tên project không được để trống")
    @Size(min = 3, max = 100, message = "Tên project phải từ 3-100 ký tự")
    private String name;

    @Size(max = 255, message = "Mô tả project không quá 255 ký tự")
    private String description;

    @NotNull(message = "Ngày bắt đầu không được null")
    private LocalDate startDate;

    private LocalDate endDate;
}
