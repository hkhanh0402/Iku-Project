package com.example.projectiku.service;

import com.example.projectiku.dto.RegisterRequest;
import com.example.projectiku.dto.UserRequest;
import com.example.projectiku.dto.UserResponse;

import java.util.List;

public interface UserService {
    List<UserResponse> findAll();

    UserResponse findById(long id);

    UserResponse add(UserRequest userRequest);

    UserResponse update(UserRequest userRequest, long id);

    void delete(long id);

    void updateRole(Long id, List<String> roles);

    UserResponse register(RegisterRequest request);
}
