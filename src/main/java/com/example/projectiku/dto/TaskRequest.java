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
public class TaskRequest{
    @NotBlank(message = "Tiêu đề task không được để trống")
    @Size(min = 3, max = 100, message = "Tiêu đề task phải từ 3-100 ký tự")
    private String title;

    @Size(max = 255, message = "Mô tả không quá 255 ký tự")
    private String description;

    @NotNull(message = "Trạng thái không được để null")
    private TaskStatus status;

    @FutureOrPresent(message = "Hạn hoàn thành phải là hiện tại hoặc tương lai")
    private LocalDate dueDate;

    private Long userId;

    private Long projectId;
}

