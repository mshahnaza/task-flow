package com.example.taskmanager.mappers;

import com.example.taskmanager.dao.UserDetailsDao;
import com.example.taskmanager.dto.response.UserResponse;
import com.example.taskmanager.entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;
import java.util.Optional;


@Mapper(componentModel = "spring")
public interface UserMapper {

    UserDetailsDao userToUserDetailsDao(User user);

    @Mapping(target = "roles", expression = "java(user.getRoles().stream().map(role -> role.getName().name()).toList())")
    UserResponse userToUserDto(Optional<User> user);

    List<UserResponse> userToUserDtos(List<User> users);
}