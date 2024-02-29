package com.example.lorby.services.impl;

import com.example.lorby.dto.request.UserRequestDTO;
import com.example.lorby.dto.response.UserResponseDTO;
import com.example.lorby.entities.Role;
import com.example.lorby.entities.User;
import com.example.lorby.exceptions.UsernameExistsException;
import com.example.lorby.repositories.UserRepository;
import com.example.lorby.services.RoleService;
import com.example.lorby.services.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService, UserDetailsService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleService roleService;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);

        if(user == null){
            log.info("User not found in database");
            throw new UsernameNotFoundException("User not found database");
        } else {
            log.info("User found in database {}", username);
        }
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        user.getRoles().forEach(role -> authorities.add(new SimpleGrantedAuthority(role.getName())));
        return new org.springframework.security.core.userdetails.User(user.getUsername(),user.getPassword(),authorities);
    }


    @Override
    public UserResponseDTO getUser(UUID id) {
        log.info("Fetching user with id {}", id);
        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("user not found"));
        return UserResponseDTO.builder()
                .username(user.getUsername())
                .email(user.getEmail())
                .build();
    }

    @Override
    public UUID register(UserRequestDTO userRequestDTO) throws UsernameExistsException{
        if (usernameExist(userRequestDTO.getUsername())) {
            throw new UsernameExistsException(userRequestDTO.getUsername());
        }
        else {
            User user = new User();
            user.setUsername(userRequestDTO.getUsername());
            user.setEmail(userRequestDTO.getEmail());
            user.setPassword(passwordEncoder.encode(userRequestDTO.getPassword()));
            Role role = roleService.findByName("USER");
            user.setRoles(List.of(role));
            return userRepository.save(user).getId();
        }
    }

    @Override
    public UUID addRoleToUser(UUID userId, String roleName) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("user not found"));
        Role role = roleService.findByName(roleName);
        user.getRoles().add(role);
        return userRepository.save(user).getId();
    }

    private boolean usernameExist(String username) {
        return userRepository.findByUsername(username) != null;
    }
}
