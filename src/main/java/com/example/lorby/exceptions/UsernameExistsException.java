package com.example.lorby.exceptions;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "User not found")
@Data
@AllArgsConstructor
public class UsernameExistsException extends Exception{
    private String username;
}
