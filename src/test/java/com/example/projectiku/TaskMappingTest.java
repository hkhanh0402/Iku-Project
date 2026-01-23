package com.example.projectiku;

import com.example.projectiku.entity.Task;
import com.example.projectiku.repository.TaskRepo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class TaskMappingTest {

    @Autowired
    private TaskRepo taskRepo;

    @Test
    @Transactional
    void testTaskUserProjectMapping(){
        Task task = taskRepo.findById(1L).orElseThrow(() -> new RuntimeException("Task not found"));

        assertNotNull(task);
        assertNotNull(task.getUser());
        assertNotNull(task.getProject());

        System.out.println("Task: " + task.getTitle());
        System.out.println("User: " + task.getUser().getUsername());
        System.out.println("Project: " + task.getProject().getName());
    }
}
