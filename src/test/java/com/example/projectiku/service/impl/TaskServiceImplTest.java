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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TaskServiceImplTest {

    @Mock
    private TaskRepo taskRepo;
    @Mock private UserRepo userRepo;
    @Mock private ProjectRepo projectRepo;
    @Mock private ModelMapper modelMapper;

    @InjectMocks
    private TaskServiceImpl taskService;

    private TaskRequest request;
    private Task task;
    private User user;
    private Project project;

    @BeforeEach
    void setup() {
        request = new TaskRequest();
        request.setTitle("Test");
        request.setDescription("Desc");
        request.setStatus(TaskStatus.TODO);
        request.setDueDate(LocalDate.now().plusDays(1));
        request.setUserId(1L);
        request.setProjectId(1L);

        task = new Task();
        user = new User(); user.setId(1L);
        project = new Project(); project.setId(1L);
    }

    @Test
    void findAll_shouldReturnList() {
        when(taskRepo.findAll()).thenReturn(List.of(task));
        when(modelMapper.map(task, TaskResponse.class)).thenReturn(new TaskResponse());

        List<TaskResponse> res = taskService.findAll();

        assertFalse(res.isEmpty());
    }

    @Test
    void findById_valid_shouldReturn() {
        when(taskRepo.findById(1L)).thenReturn(Optional.of(task));
        when(modelMapper.map(task, TaskResponse.class)).thenReturn(new TaskResponse());

        assertNotNull(taskService.findById(1L));
    }

    @Test
    void findById_notFound_shouldThrow() {
        when(taskRepo.findById(1L)).thenReturn(Optional.empty());

        assertThrows(CustomResourceNotFoundException.class,
                () -> taskService.findById(1L));
    }

    @Test
    void add_valid_shouldSuccess() {
        when(modelMapper.map(request, Task.class)).thenReturn(task);
        when(userRepo.findById(1L)).thenReturn(Optional.of(user));
        when(projectRepo.findById(1L)).thenReturn(Optional.of(project));
        when(projectRepo.existsByIdAndUsers_Id(1L,1L)).thenReturn(true);
        when(taskRepo.save(task)).thenReturn(task);
        when(modelMapper.map(task, TaskResponse.class)).thenReturn(new TaskResponse());

        assertNotNull(taskService.add(request));
        verify(taskRepo).save(task);
    }

    @Test
    void add_userNull_shouldThrow() {
        request.setUserId(null);
        assertThrows(CustomBadRequestException.class, () -> taskService.add(request));
    }

    @Test
    void add_projectNull_shouldThrow() {
        request.setProjectId(null);
        assertThrows(CustomBadRequestException.class, () -> taskService.add(request));
    }

    @Test
    void add_userNotFound_shouldThrow() {
        when(modelMapper.map(request, Task.class)).thenReturn(task);
        when(userRepo.findById(1L)).thenReturn(Optional.empty());

        assertThrows(CustomResourceNotFoundException.class, () -> taskService.add(request));
    }

    @Test
    void add_projectNotFound_shouldThrow() {
        when(modelMapper.map(request, Task.class)).thenReturn(task);
        when(userRepo.findById(1L)).thenReturn(Optional.of(user));
        when(projectRepo.findById(1L)).thenReturn(Optional.empty());

        assertThrows(CustomResourceNotFoundException.class, () -> taskService.add(request));
    }

    @Test
    void add_userNotInProject_shouldThrow() {
        when(modelMapper.map(request, Task.class)).thenReturn(task);
        when(userRepo.findById(1L)).thenReturn(Optional.of(user));
        when(projectRepo.findById(1L)).thenReturn(Optional.of(project));
        when(projectRepo.existsByIdAndUsers_Id(1L,1L)).thenReturn(false);

        assertThrows(CustomBadRequestException.class, () -> taskService.add(request));
    }

    @Test
    void add_dueDatePast_shouldThrow() {
        request.setDueDate(LocalDate.now().minusDays(1));

        when(modelMapper.map(request, Task.class)).thenReturn(task);
        when(userRepo.findById(1L)).thenReturn(Optional.of(user));
        when(projectRepo.findById(1L)).thenReturn(Optional.of(project));
        when(projectRepo.existsByIdAndUsers_Id(1L,1L)).thenReturn(true);

        assertThrows(CustomBadRequestException.class, () -> taskService.add(request));
    }

    @Test
    void update_valid_shouldSuccess() {
        task.setStatus(TaskStatus.TODO);

        when(taskRepo.findById(1L)).thenReturn(Optional.of(task));
        when(taskRepo.save(task)).thenReturn(task);
        when(modelMapper.map(task, TaskResponse.class)).thenReturn(new TaskResponse());

        assertNotNull(taskService.update(request, 1L));
    }

    @Test
    void update_notFound_shouldThrow() {
        when(taskRepo.findById(1L)).thenReturn(Optional.empty());

        assertThrows(CustomResourceNotFoundException.class,
                () -> taskService.update(request, 1L));
    }

    @Test
    void update_taskDone_shouldThrow() {
        task.setStatus(TaskStatus.DONE);
        when(taskRepo.findById(1L)).thenReturn(Optional.of(task));

        assertThrows(CustomBadRequestException.class,
                () -> taskService.update(request, 1L));
    }

    @Test
    void update_dueDatePast_shouldThrow() {
        task.setStatus(TaskStatus.TODO);
        request.setDueDate(LocalDate.now().minusDays(1));

        when(taskRepo.findById(1L)).thenReturn(Optional.of(task));

        assertThrows(CustomBadRequestException.class,
                () -> taskService.update(request, 1L));
    }

    @Test
    void update_userNotInProject_shouldThrow() {
        task.setStatus(TaskStatus.TODO);
        task.setProject(project);

        request.setUserId(2L);

        User newUser = new User(); newUser.setId(2L);

        when(taskRepo.findById(1L)).thenReturn(Optional.of(task));
        when(userRepo.findById(2L)).thenReturn(Optional.of(newUser));
        when(projectRepo.existsByIdAndUsers_Id(1L,2L)).thenReturn(false);

        assertThrows(CustomBadRequestException.class,
                () -> taskService.update(request, 1L));
    }

    @Test
    void delete_valid_shouldCallRepo() {
        when(taskRepo.findById(1L)).thenReturn(Optional.of(task));

        taskService.delete(1L);

        verify(taskRepo).delete(task);
    }

    @Test
    void delete_notFound_shouldThrow() {
        when(taskRepo.findById(1L)).thenReturn(Optional.empty());

        assertThrows(CustomResourceNotFoundException.class,
                () -> taskService.delete(1L));
    }

    @Test
    void assignTask_valid_shouldSuccess() {
        task.setStatus(TaskStatus.TODO);
        task.setProject(project);

        when(taskRepo.findById(1L)).thenReturn(Optional.of(task));
        when(userRepo.findById(1L)).thenReturn(Optional.of(user));
        when(projectRepo.existsByIdAndUsers_Id(1L,1L)).thenReturn(true);
        when(taskRepo.save(task)).thenReturn(task);
        when(modelMapper.map(task, TaskResponse.class)).thenReturn(new TaskResponse());

        TaskResponse res = taskService.assignTask(1L,1L);

        assertNotNull(res);
        assertEquals(user, task.getUser());
    }

    @Test
    void assignTask_projectNull_shouldThrow() {
        task.setProject(null);

        when(taskRepo.findById(1L)).thenReturn(Optional.of(task));
        when(userRepo.findById(1L)).thenReturn(Optional.of(user));

        assertThrows(CustomBadRequestException.class,
                () -> taskService.assignTask(1L,1L));
    }

    @Test
    void assignTask_taskNotFound_shouldThrow() {
        when(taskRepo.findById(1L)).thenReturn(Optional.empty());

        assertThrows(CustomResourceNotFoundException.class,
                () -> taskService.assignTask(1L,1L));
    }

    @Test
    void assignTask_userNotFound_shouldThrow() {
        when(taskRepo.findById(1L)).thenReturn(Optional.of(task));
        when(userRepo.findById(1L)).thenReturn(Optional.empty());

        assertThrows(CustomResourceNotFoundException.class,
                () -> taskService.assignTask(1L,1L));
    }

    @Test
    void assignTask_taskDone_shouldThrow() {
        task.setStatus(TaskStatus.DONE);
        task.setProject(project);

        when(taskRepo.findById(1L)).thenReturn(Optional.of(task));
        when(userRepo.findById(1L)).thenReturn(Optional.of(user));

        assertThrows(CustomBadRequestException.class,
                () -> taskService.assignTask(1L,1L));
    }

    @Test
    void assignTask_userNotInProject_shouldThrow() {
        task.setStatus(TaskStatus.TODO);
        task.setProject(project);

        when(taskRepo.findById(1L)).thenReturn(Optional.of(task));
        when(userRepo.findById(1L)).thenReturn(Optional.of(user));
        when(projectRepo.existsByIdAndUsers_Id(1L,1L)).thenReturn(false);

        assertThrows(CustomBadRequestException.class,
                () -> taskService.assignTask(1L,1L));
    }

    @Test
    void findByUserId_valid_shouldReturnList() {
        when(userRepo.findById(1L)).thenReturn(Optional.of(user));
        when(taskRepo.findByUserId(1L)).thenReturn(List.of(task));
        when(modelMapper.map(task, TaskResponse.class)).thenReturn(new TaskResponse());

        assertFalse(taskService.findByUserId(1L).isEmpty());
    }

    @Test
    void findByUserId_notFound_shouldThrow() {
        when(userRepo.findById(1L)).thenReturn(Optional.empty());

        assertThrows(CustomResourceNotFoundException.class,
                () -> taskService.findByUserId(1L));
    }

    @Test
    void findByProjectId_valid_shouldReturnList() {
        when(projectRepo.findById(1L)).thenReturn(Optional.of(project));
        when(taskRepo.findByProjectId(1L)).thenReturn(List.of(task));
        when(modelMapper.map(task, TaskResponse.class)).thenReturn(new TaskResponse());

        assertFalse(taskService.findByProjectId(1L).isEmpty());
    }

    @Test
    void findByProjectId_notFound_shouldThrow() {
        when(projectRepo.findById(1L)).thenReturn(Optional.empty());

        assertThrows(CustomResourceNotFoundException.class,
                () -> taskService.findByProjectId(1L));
    }
}
