package com.example.userauthenticationservice.controller;

import com.example.userauthenticationservice.dtos.LoginRequestDto;
import com.example.userauthenticationservice.dtos.SignupRequestDto;
import com.example.userauthenticationservice.dtos.UserDto;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {



    @PostMapping("/signup")
public UserDto signUp(@RequestBody SignupRequestDto signupRequestDto) {
return null;
}

@PostMapping("/login")
public UserDto login(@RequestBody LoginRequestDto loginRequestDto ){
    return null;
}

}
