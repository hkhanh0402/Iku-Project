package com.example.projectiku.service.impl;

import com.example.projectiku.dto.TaskRequest;
import com.example.projectiku.dto.TaskResponse;
import com.example.projectiku.entity.Project;
import com.example.projectiku.entity.Task;
import com.example.projectiku.entity.User;
import com.example.projectiku.exception.CustomBadRequestException;
import com.example.projectiku.exception.CustomResourceNotFoundException;
import com.example.projectiku.repository.ProjectRepo;
import com.example.projectiku.repository.TaskRepo;
import com.example.projectiku.repository.UserRepo;
import com.example.projectiku.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {
    private final TaskRepo taskRepo;
    private final UserRepo userRepo;
    private final ProjectRepo projectRepo;

    private final ModelMapper modelMapper;

    @Override
    public List<TaskResponse> findAll() {
        return taskRepo.findAll().stream().map(e -> modelMapper.map(e, TaskResponse.class)).toList();
    }

    @Override
    public TaskResponse findById(long id) {
        return taskRepo.findById(id).map(e -> modelMapper.map(e, TaskResponse.class))
                .orElseThrow(() -> new CustomResourceNotFoundException("Task not found with id " + id));
    }

    @Override
    public TaskResponse add(TaskRequest taskRequest) {
        if (taskRequest.getUserId() == null){
            throw new CustomBadRequestException("userId is required");
        }

        if (taskRequest.getProjectId() == null){
            throw new CustomBadRequestException("projectId is required");
        }

        Task task = modelMapper.map(taskRequest, Task.class);

        User user = userRepo.findById(taskRequest.getUserId())
                .orElseThrow(() -> new CustomResourceNotFoundException("User not found with id " + taskRequest.getUserId()));

        Project project = projectRepo.findById(taskRequest.getProjectId())
                .orElseThrow(() -> new CustomResourceNotFoundException("Project not found with id " + taskRequest.getProjectId()));

        task.setUser(user);
        task.setProject(project);

        Task savedTask = taskRepo.save(task);
        return modelMapper.map(savedTask, TaskResponse.class);
    }

    @Override
    public TaskResponse update(TaskRequest taskRequest, long id) {
        return taskRepo.findById(id).map(e -> {
            if (taskRequest.getTitle() != null) e.setTitle(taskRequest.getTitle());

            if (taskRequest.getDescription() != null) e.setDescription(taskRequest.getDescription());

            if (taskRequest.getStatus() != null) e.setStatus(taskRequest.getStatus());

            if (taskRequest.getDueDate() != null) e.setDueDate(taskRequest.getDueDate());

            if (taskRequest.getUserId() != null) e.setUser(userRepo.findById(taskRequest.getUserId())
                    .orElseThrow(() -> new CustomResourceNotFoundException("User not found with id " + taskRequest.getUserId())));

            if (taskRequest.getProjectId() != null) e.setProject(projectRepo.findById(taskRequest.getProjectId())
                    .orElseThrow(() -> new CustomResourceNotFoundException("Project not found with id " + taskRequest.getProjectId())));

            taskRepo.save(e);
            return modelMapper.map(e, TaskResponse.class);
        }).orElseThrow(() -> new CustomResourceNotFoundException("Task not found with id " + id));
    }

    @Override
    public void delete(long id) {
        taskRepo.findById(id).orElseThrow(() -> new CustomResourceNotFoundException("Task not found with id " + id));
        taskRepo.deleteById(id);
    }

    @Override
    public List<TaskResponse> findByUserId(long userId) {
        userRepo.findById(userId).orElseThrow(() -> new CustomResourceNotFoundException("User not found with id " + userId));

        return taskRepo.findByUserId(userId).stream().map(e -> modelMapper.map(e, TaskResponse.class)).toList();
    }

    @Override
    public List<TaskResponse> findByProjectId(long projectId) {
        userRepo.findById(projectId).orElseThrow(() -> new CustomResourceNotFoundException("User not found with id " + projectId));

        return taskRepo.findByProjectId(projectId).stream().map(e -> modelMapper.map(e, TaskResponse.class)).toList();
    }
}
