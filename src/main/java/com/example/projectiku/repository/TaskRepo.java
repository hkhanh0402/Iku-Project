package com.example.projectiku.repository;

import com.example.projectiku.entity.Task;
import com.example.projectiku.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskRepo extends JpaRepository<Task, Long> {
    List<Task> findByUserId(long userId);

    List<Task> findByProjectId(long projectId);

}
