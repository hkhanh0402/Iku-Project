package com.example.projectiku.service.impl;

import com.example.projectiku.dto.RegisterRequest;
import com.example.projectiku.dto.UserRequest;
import com.example.projectiku.dto.UserResponse;
import com.example.projectiku.entity.Role;
import com.example.projectiku.entity.User;
import com.example.projectiku.enums.RoleName;
import com.example.projectiku.exception.CustomDuplicateResourceException;
import com.example.projectiku.exception.CustomResourceNotFoundException;
import com.example.projectiku.repository.RoleRepo;
import com.example.projectiku.repository.UserRepo;
import com.example.projectiku.service.UserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepo userRepo;
    private final ModelMapper modelMapper;
    private final RoleRepo roleRepo;
    private final PasswordEncoder passwordEncoder;

    @Override
    public List<UserResponse> findAll() {
        return userRepo.findAll()
                .stream()
                .map(user -> modelMapper.map(user, UserResponse.class))
                .toList();
    }

    @Override
    public UserResponse findById(long id) {
        User user = userRepo.findById(id)
                .orElseThrow(() ->
                        new CustomResourceNotFoundException("User not found with id " + id));

        return modelMapper.map(user, UserResponse.class);
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
        user.setPassword(passwordEncoder.encode(userRequest.getPassword()));

        if (userRequest.getRoles() != null && !userRequest.getRoles().isEmpty()) {

            List<Role> roles = userRequest.getRoles().stream()
                    .map(name -> roleRepo.findByName(RoleName.valueOf(name.toUpperCase()))
                            .orElseThrow(() ->
                                    new CustomResourceNotFoundException("Role not found: " + name)))
                    .toList();

            user.setRoles(roles);

        } else {
            Role userRole = roleRepo.findByName(RoleName.USER)
                    .orElseThrow(() ->
                            new CustomResourceNotFoundException("Role USER not found"));

            user.setRoles(List.of(userRole));
        }

        User savedUser = userRepo.save(user);

        return modelMapper.map(savedUser, UserResponse.class);
    }

    @Override
    public UserResponse update(UserRequest userRequest, long id) {

        User user = userRepo.findById(id)
                .orElseThrow(() ->
                        new CustomResourceNotFoundException("User not found with id " + id));

        if (userRequest.getUsername() != null &&
                userRepo.existsByUsernameAndIdNot(userRequest.getUsername(), id)) {
            throw new CustomDuplicateResourceException("Username already exists");
        }

        if (userRequest.getEmail() != null &&
                userRepo.existsByEmailAndIdNot(userRequest.getEmail(), id)) {
            throw new CustomDuplicateResourceException("Email already exists");
        }

        if (userRequest.getUsername() != null)
            user.setUsername(userRequest.getUsername());

        if (userRequest.getPassword() != null)
            user.setPassword(passwordEncoder.encode(userRequest.getPassword()));

        if (userRequest.getFullName() != null)
            user.setFullName(userRequest.getFullName());

        if (userRequest.getEmail() != null)
            user.setEmail(userRequest.getEmail());

        if (userRequest.getRoles() != null) {
            List<Role> roles = userRequest.getRoles().stream()
                    .map(name -> roleRepo.findByName(RoleName.valueOf(name.toUpperCase()))
                            .orElseThrow(() ->
                                    new CustomResourceNotFoundException("Role not found: " + name)))
                    .toList();

            user.setRoles(roles);
        }

        userRepo.save(user);

        return modelMapper.map(user, UserResponse.class);
    }

    @Override
    public void delete(long id) {
        if (!userRepo.existsById(id)) {
            throw new CustomResourceNotFoundException("User not found with id " + id);
        }
        userRepo.deleteById(id);
    }

    @Override
    public void updateRole(Long userId, List<String> roleNames) {

        User user = userRepo.findById(userId)
                .orElseThrow(() ->
                        new CustomResourceNotFoundException("User not found"));

        List<Role> roles = roleNames.stream()
                .map(name -> roleRepo.findByName(RoleName.valueOf(name.toUpperCase()))
                        .orElseThrow(() ->
                                new CustomResourceNotFoundException("Role not found: " + name)))
                .toList();

        user.setRoles(roles);
        userRepo.save(user);
    }

    @Override
    public UserResponse register(RegisterRequest request) {

        if (userRepo.existsByUsername(request.getUsername())) {
            throw new CustomDuplicateResourceException("Username already exists");
        }

        if (userRepo.existsByEmail(request.getEmail())) {
            throw new CustomDuplicateResourceException("Email already exists");
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setFullName(request.getFullName());
        user.setEmail(request.getEmail());

        Role userRole = roleRepo.findByName(RoleName.USER)
                .orElseThrow(() ->
                        new CustomResourceNotFoundException("Role USER not found"));

        user.setRoles(List.of(userRole));

        User savedUser = userRepo.save(user);

        return modelMapper.map(savedUser, UserResponse.class);
    }
}