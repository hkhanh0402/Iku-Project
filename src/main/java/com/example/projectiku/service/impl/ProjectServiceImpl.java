package com.example.projectiku.service.impl;

import com.example.projectiku.dto.ProjectRequest;
import com.example.projectiku.dto.ProjectResponse;
import com.example.projectiku.entity.Project;
import com.example.projectiku.entity.User;
import com.example.projectiku.exception.CustomDuplicateResourceException;
import com.example.projectiku.exception.CustomResourceNotFoundException;
import com.example.projectiku.repository.ProjectRepo;
import com.example.projectiku.repository.UserRepo;
import com.example.projectiku.service.ProjectService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepo projectRepo;
    private final UserRepo userRepo;
    private final ModelMapper modelMapper;

    @Override
    public List<ProjectResponse> findAll() {
        return projectRepo.findAll()
                .stream()
                .map(project -> modelMapper.map(project, ProjectResponse.class))
                .toList();
    }

    @Override
    public ProjectResponse findById(long id) {
        return projectRepo.findById(id)
                .map(project -> modelMapper.map(project, ProjectResponse.class))
                .orElseThrow(() ->
                        new CustomResourceNotFoundException("Project not found with id " + id));
    }

    @Override
    public ProjectResponse add(ProjectRequest projectRequest) {

        if (projectRepo.existsByName(projectRequest.getName())) {
            throw new CustomDuplicateResourceException("Project name already exists");
        }

        Project project = modelMapper.map(projectRequest, Project.class);

        Project savedProject = projectRepo.save(project);

        return modelMapper.map(savedProject, ProjectResponse.class);
    }

    @Override
    public ProjectResponse update(ProjectRequest projectRequest, long id) {

        return projectRepo.findById(id).map(e -> {

            if (projectRequest.getName() != null &&
                    projectRepo.existsByNameAndIdNot(projectRequest.getName(), id)) {
                throw new CustomDuplicateResourceException("Project name already exists");
            }

            if (projectRequest.getName() != null)
                e.setName(projectRequest.getName());

            if (projectRequest.getDescription() != null)
                e.setDescription(projectRequest.getDescription());

            if (projectRequest.getStartDate() != null)
                e.setStartDate(projectRequest.getStartDate());

            if (projectRequest.getEndDate() != null)
                e.setEndDate(projectRequest.getEndDate());

            projectRepo.save(e);

            return modelMapper.map(e, ProjectResponse.class);

        }).orElseThrow(() ->
                new CustomResourceNotFoundException("Project not found with id " + id));
    }

    @Override
    public void delete(long id) {
        projectRepo.findById(id)
                .orElseThrow(() ->
                        new CustomResourceNotFoundException("Project not found with id " + id));

        projectRepo.deleteById(id);
    }

    @Override
    @Transactional
    public void addUserToProject(long projectId, long userId) {

        Project project = projectRepo.findById(projectId)
                .orElseThrow(() ->
                        new CustomResourceNotFoundException("Project not found with id " + projectId));

        User user = userRepo.findById(userId)
                .orElseThrow(() ->
                        new CustomResourceNotFoundException("User not found with id " + userId));

        if (project.getUsers().contains(user)) {
            throw new CustomDuplicateResourceException("User already in project");
        }

        project.getUsers().add(user);
        user.getProjects().add(project);
    }
}