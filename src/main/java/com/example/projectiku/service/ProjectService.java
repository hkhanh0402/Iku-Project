package com.example.projectiku.service;

import com.example.projectiku.dto.ProjectRequest;
import com.example.projectiku.dto.ProjectResponse;

import java.util.List;

public interface ProjectService {
    List<ProjectResponse> findAll();

    ProjectResponse findById(long id);

    ProjectResponse add(ProjectRequest projectRequest);

    ProjectResponse update(ProjectRequest projectRequest, long id);

    void delete(long id);

    void addUserToProject(long projectId, long userId);
}
