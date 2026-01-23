package com.example.projectiku.service.impl;

import com.example.projectiku.dto.UserRequest;
import com.example.projectiku.dto.UserResponse;
import com.example.projectiku.entity.User;
import com.example.projectiku.exception.CustomDuplicateResourceException;
import com.example.projectiku.exception.CustomResourceNotFoundException;
import com.example.projectiku.repository.UserRepo;
import com.example.projectiku.service.UserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepo userRepo;

    private final ModelMapper modelMapper;

    @Override
    public List<UserResponse> findAll() {
        return userRepo.findAll().stream().map(user -> modelMapper.map(user, UserResponse.class)).toList();
    }

    @Override
    public UserResponse findById(long id) {
        return userRepo.findById(id).map(user -> modelMapper.map(user, UserResponse.class))
                .orElseThrow(() -> new CustomResourceNotFoundException("User not found with id " + id));
    }

    @Override
    public UserResponse add(UserRequest userRequest) {
        if (userRepo.existsByUsername(userRequest.getUsername())) {
            throw new CustomDuplicateResourceException("Username already exists");
        }

        if (userRepo.existsByEmail(userRequest.getEmail())) {
            throw new CustomDuplicateResourceException("Email already exists");
        }

        User user = modelMapper.map(userRequest, User.class);

        User savedUser = userRepo.save(user);
        return modelMapper.map(savedUser, UserResponse.class);
    }

    @Override
    public UserResponse update(UserRequest userRequest, long id) {
        return userRepo.findById(id).map(e -> {
            if (userRequest.getUsername() != null &&
            userRepo.existsByUsernameAndIdNot(userRequest.getUsername(), id)) {
                throw new CustomDuplicateResourceException("Username already exists");
            }

            if (userRequest.getEmail() != null &&
            userRepo.existsByEmailAndIdNot(userRequest.getEmail(), id)) {
                throw new CustomDuplicateResourceException("Email already exists");
            }

            if (userRequest.getUsername() != null) e.setUsername(userRequest.getUsername());
            if (userRequest.getFullName() != null) e.setFullName(userRequest.getFullName());
            if (userRequest.getEmail() != null) e.setEmail(userRequest.getEmail());

            userRepo.save(e);
            return modelMapper.map(e, UserResponse.class);
        }).orElseThrow(() -> new CustomResourceNotFoundException("User not found with id " + id));
    }

    @Override
    public void delete(long id) {
        userRepo.findById(id).orElseThrow(() -> new CustomResourceNotFoundException("User not found with id " + id));
        userRepo.deleteById(id);
    }
}
