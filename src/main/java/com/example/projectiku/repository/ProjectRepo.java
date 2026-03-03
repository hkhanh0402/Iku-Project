package com.example.projectiku.repository;

import com.example.projectiku.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectRepo extends JpaRepository<Project, Long> {
    boolean existsByIdAndUsers_Id(Long projectId, Long userId);

    boolean existsByName(String name);

    boolean existsByNameAndIdNot(String name, long id);
}
