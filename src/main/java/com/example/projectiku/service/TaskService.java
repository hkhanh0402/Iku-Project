package com.example.projectiku.service;

import com.example.projectiku.dto.TaskRequest;
import com.example.projectiku.dto.TaskResponse;

import java.util.List;

public interface TaskService {
    List<TaskResponse> findAll();

    TaskResponse findById(long id);

    TaskResponse add(TaskRequest taskRequest);

    TaskResponse update(TaskRequest taskRequest, long id);

    void delete(long id);

    List<TaskResponse> findByUserId(long userId);

    List<TaskResponse> findByProjectId(long projectId);

    TaskResponse assignTask(long taskId, long userId);

    List<TaskResponse> getMyTasks();
}
