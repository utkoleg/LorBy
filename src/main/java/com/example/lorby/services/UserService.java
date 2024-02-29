package com.example.lorby.services;

import com.example.lorby.dto.request.UserRequestDTO;
import com.example.lorby.dto.response.UserResponseDTO;
import com.example.lorby.exceptions.UsernameExistsException;

import java.util.UUID;

public interface UserService {
    UserResponseDTO getUser(UUID id);
    UUID register(UserRequestDTO userRequestDTO) throws UsernameExistsException;

    UUID addRoleToUser(UUID userId, String roleName);
}
