package com.example.projectiku.config;

import com.example.projectiku.dto.TaskRequest;
import com.example.projectiku.dto.TaskResponse;
import com.example.projectiku.entity.Task;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();

        modelMapper.getConfiguration()
                .setMatchingStrategy(MatchingStrategies.STRICT);

        // Request -> Entity
        modelMapper.typeMap(TaskRequest.class, Task.class)
                .addMappings(mapper -> {
                    mapper.skip(Task::setId);
                    mapper.skip(Task::setUser);
                    mapper.skip(Task::setProject);
                });

        // Entity -> Response
        modelMapper.typeMap(Task.class, TaskResponse.class)
                .addMappings(mapper -> {
                    mapper.map(src -> src.getUser().getId(),
                            TaskResponse::setUserId);

                    mapper.map(src -> src.getProject().getId(),
                            TaskResponse::setProjectId);
                });

        return modelMapper;
    }
}

