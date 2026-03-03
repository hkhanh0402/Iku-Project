package com.example.projectiku.config;

import com.example.projectiku.dto.TaskRequest;
import com.example.projectiku.dto.TaskResponse;
import com.example.projectiku.dto.UserResponse;
import com.example.projectiku.entity.Task;
import com.example.projectiku.entity.User;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class AppConfig {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();

        modelMapper.getConfiguration()
                .setMatchingStrategy(MatchingStrategies.STRICT);


        modelMapper.typeMap(TaskRequest.class, Task.class)
                .addMappings(mapper -> {
                    mapper.skip(Task::setId);
                    mapper.skip(Task::setUser);
                    mapper.skip(Task::setProject);
                });

        modelMapper.typeMap(Task.class, TaskResponse.class)
                .addMappings(mapper -> {
                    mapper.map(src -> src.getUser().getId(),
                            TaskResponse::setUserId);

                    mapper.map(src -> src.getProject().getId(),
                            TaskResponse::setProjectId);
                });

        modelMapper.typeMap(User.class, UserResponse.class)
                .addMappings(mapper -> mapper.skip(UserResponse::setRoles))
                .setPostConverter(context -> {

                    User source = context.getSource();
                    UserResponse destination = context.getDestination();

                    destination.setRoles(
                            source.getRoles() == null
                                    ? List.of()
                                    : source.getRoles()
                                    .stream()
                                    .map(role -> role.getName().name())
                                    .toList()
                    );

                    return destination;
                });

        return modelMapper;
    }
}