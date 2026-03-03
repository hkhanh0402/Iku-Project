package com.example.projectiku.service.impl;

import com.example.projectiku.dto.TaskRequest;
import com.example.projectiku.dto.TaskResponse;
import com.example.projectiku.entity.Project;
import com.example.projectiku.entity.Task;
import com.example.projectiku.entity.User;
import com.example.projectiku.enums.TaskStatus;
import com.example.projectiku.exception.CustomBadRequestException;
import com.example.projectiku.exception.CustomResourceNotFoundException;
import com.example.projectiku.repository.ProjectRepo;
import com.example.projectiku.repository.TaskRepo;
import com.example.projectiku.repository.UserRepo;
import com.example.projectiku.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
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

        if (!projectRepo.existsByIdAndUsers_Id(project.getId(), user.getId())) {
            throw new CustomBadRequestException("User doesn't belong to this project");
        }

        if (taskRequest.getDueDate().isBefore(LocalDate.now())) {
            throw new CustomBadRequestException("Due date must be in the future");
        }

        task.setUser(user);
        task.setProject(project);

        Task savedTask = taskRepo.save(task);
        return modelMapper.map(savedTask, TaskResponse.class);
    }

    @Override
    public TaskResponse update(TaskRequest taskRequest, long id) {

        return taskRepo.findById(id).map(e -> {

            if (e.getStatus() == TaskStatus.DONE) {
                throw new CustomBadRequestException("Cannot update. Task is already DONE");
            }

            if (taskRequest.getTitle() != null)
                e.setTitle(taskRequest.getTitle());

            if (taskRequest.getDescription() != null)
                e.setDescription(taskRequest.getDescription());

            if (taskRequest.getStatus() != null)
                e.setStatus(taskRequest.getStatus());

            if (taskRequest.getDueDate() != null) {
                if (taskRequest.getDueDate().isBefore(LocalDate.now())) {
                    throw new CustomBadRequestException("Due date must be in the future");
                }
                e.setDueDate(taskRequest.getDueDate());
            }

            if (taskRequest.getUserId() != null) {

                User user = userRepo.findById(taskRequest.getUserId())
                        .orElseThrow(() ->
                                new CustomResourceNotFoundException("User not found with id " + taskRequest.getUserId()));

                if (!projectRepo.existsByIdAndUsers_Id(e.getProject().getId(), user.getId())) {
                    throw new CustomBadRequestException("User doesn't belong to this project");
                }

                e.setUser(user);
            }

            return modelMapper.map(taskRepo.save(e), TaskResponse.class);

        }).orElseThrow(() ->
                new CustomResourceNotFoundException("Task not found with id " + id));
    }

    @Override
    public void delete(long id) {
        Task task = taskRepo.findById(id)
                .orElseThrow(() -> new CustomResourceNotFoundException("Task not found with id " + id));

        taskRepo.delete(task);
    }

    @Override
    public List<TaskResponse> findByUserId(long userId) {
        userRepo.findById(userId).orElseThrow(() -> new CustomResourceNotFoundException("User not found with id " + userId));

        return taskRepo.findByUserId(userId).stream().map(e -> modelMapper.map(e, TaskResponse.class)).toList();
    }

    @Override
    public List<TaskResponse> findByProjectId(long projectId) {
        projectRepo.findById(projectId).orElseThrow(() -> new CustomResourceNotFoundException("Project not found with id " + projectId));

        return taskRepo.findByProjectId(projectId).stream().map(e -> modelMapper.map(e, TaskResponse.class)).toList();
    }

    @Override
    public TaskResponse assignTask(long taskId, long userId) {
        Task task = taskRepo.findById(taskId).orElseThrow(() -> new CustomResourceNotFoundException("Task not found with id " + taskId));

        User user = userRepo.findById(userId).orElseThrow(() -> new CustomResourceNotFoundException("User not found with id " + userId));

        Project project = task.getProject();

        if (project == null){
            throw new CustomBadRequestException("Task doesn't belong to any project");
        }

        if (task.getStatus() == TaskStatus.DONE){
            throw new CustomBadRequestException("Task is already completed");
        }

        if(!projectRepo.existsByIdAndUsers_Id(project.getId(), userId)){
            throw new CustomBadRequestException("User doesn't belong to this project");
        }

        task.setUser(user);

        return modelMapper.map(taskRepo.save(task), TaskResponse.class);
    }

    @Override
    public List<TaskResponse> getMyTasks() {

        String username = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();

        User user = userRepo.findByUsername(username)
                .orElseThrow(() -> new CustomResourceNotFoundException("User not found"));

        return taskRepo.findByUserId(user.getId())
                .stream()
                .map(task -> modelMapper.map(task, TaskResponse.class))
                .toList();
    }
}
