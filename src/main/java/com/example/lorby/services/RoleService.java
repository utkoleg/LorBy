package com.example.lorby.services;


import com.example.lorby.entities.Role;

public interface RoleService {
    Role save(String name);
    Role findByName(String name);
}