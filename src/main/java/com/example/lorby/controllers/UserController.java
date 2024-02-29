package com.example.lorby.controllers;

import com.example.lorby.dto.request.UserRequestDTO;
import com.example.lorby.dto.response.UserResponseDTO;
import com.example.lorby.exceptions.EmailExistsException;
import com.example.lorby.exceptions.UsernameExistsException;
import com.example.lorby.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/utkrent")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class UserController {
    private final UserService userService;

    @PostMapping("/sign-up")
    public UUID signNewUser(@RequestBody UserRequestDTO userRequestDto) throws UsernameExistsException, EmailExistsException {
        return userService.register(userRequestDto);
    }

    @PostMapping("/add-role-to-user")
    public UUID addRoleToUser(@RequestParam UUID userId,
                              @RequestParam String roleName) {
        return userService.addRoleToUser(userId, roleName);
    }

    @GetMapping("/{userId}")
    public UserResponseDTO getUser(@PathVariable UUID userId){
        return userService.getUser(userId);
    }

}
