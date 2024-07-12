package com.objectville.userservice.controllers;

import com.objectville.userservice.dtos.LogInRequestDto;
import com.objectville.userservice.dtos.LogOutRequestDto;
import com.objectville.userservice.dtos.SignUpRequestDto;
import com.objectville.userservice.dtos.UserDto;
import com.objectville.userservice.models.Token;
import com.objectville.userservice.models.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.objectville.userservice.services.UserService;

@RestController
@RequestMapping("/users")
public class UserController {

    private UserService userService;

     UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/signup")
    public UserDto signup(@RequestBody SignUpRequestDto requestDto) {
        User user = userService.signup(
                requestDto.getName(),
                requestDto.getEmail(),
                requestDto.getPassword());

        return UserDto.from(user);
    }


    @PostMapping("/login")
    public Token login(@RequestBody LogInRequestDto requestDto) {
        return null;
    }


    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@RequestBody LogOutRequestDto requestDto) {
        return null;
    }



    @PostMapping("/validate/{token}")
    public UserDto validateToken(@PathVariable String token) {
        return null;
    }
}