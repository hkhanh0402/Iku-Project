package com.example.projectiku.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserRequest {
    @NotBlank(message = "Username không được để trống")
    @Size(min = 3, max = 30, message = "Username phải từ 3-30 ký tự")
    private String username;

    @NotBlank(message = "Họ tên không được để trống")
    @Size(min = 3, max = 30, message = "Họ tên phải từ 3-30 ký tự")
    private String fullName;

    @NotBlank(message = "Email không được để trống")
    @Email(message = "Email không đúng định dạng")
    private String email;
}
