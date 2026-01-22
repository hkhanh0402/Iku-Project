package com.example.projectiku.controller;

import com.example.projectiku.dto.TaskRequest;
import com.example.projectiku.dto.TaskResponse;
import com.example.projectiku.service.TaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
public class TaskController {
    private final TaskService taskService;

    @GetMapping
    public ResponseEntity<List<TaskResponse>> findAll(){
        return ResponseEntity.status(HttpStatus.OK).body(taskService.findAll());
    }

    @GetMapping("{id}")
    public ResponseEntity<TaskResponse> findById(@PathVariable Long id){
        return ResponseEntity.status(HttpStatus.OK).body(taskService.findById(id));
    }

    @PostMapping
    public ResponseEntity<TaskResponse> add(@Valid @RequestBody TaskRequest taskRequest){
        return ResponseEntity.status(HttpStatus.CREATED).body(taskService.add(taskRequest));
    }

    @PutMapping("{id}")
    public ResponseEntity<TaskResponse> update(@Valid @RequestBody TaskRequest taskRequest, @PathVariable Long id){
        return ResponseEntity.status(HttpStatus.OK).body(taskService.update(taskRequest, id));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<TaskResponse> delete(@PathVariable Long id){
        taskService.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
