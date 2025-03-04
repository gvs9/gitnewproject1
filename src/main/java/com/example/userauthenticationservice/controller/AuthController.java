package com.example.userauthenticationservice.controller;

import com.example.userauthenticationservice.dtos.*;
import com.example.userauthenticationservice.exceptions.UserAlreadyExistException;
import com.example.userauthenticationservice.models.User;
import com.example.userauthenticationservice.services.AuthService;
import com.example.userauthenticationservice.services.IAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

@Autowired
    private IAuthService  authService;


    @PostMapping("/signup")
    public ResponseEntity<UserDto> signup(@RequestBody SignupRequestDto signupRequestDto) {

        try {
    User user = authService.signup(signupRequestDto.getEmail(), signupRequestDto.getPassword());
    if (user == null) {

        throw new UserAlreadyExistException("please try diiferent email");
    }

    return new ResponseEntity<>(from(user), HttpStatus.CREATED);

} catch (UserAlreadyExistException e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
}
    }

    @PostMapping("/login")

    public ResponseEntity<UserDto> login(@RequestBody LoginRequestDto loginRequestDto) {
        try {
            User user = authService.login(loginRequestDto.getEmail(), loginRequestDto.getPassword());
            if (user == null) {

                throw new UserAlreadyExistException("Bad Credentials");
            }
            // return from(user);
             return new ResponseEntity<>(from(user), HttpStatus.OK);
        }
        catch (UserAlreadyExistException e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }


    public ResponseEntity<Boolean> logout(@RequestBody LogoutRequestDto logoutRequestDto) {
        return null;
    }

    public ResponseEntity<Boolean> forgetPassword(@RequestBody ForgetPasswordDto forgetPasswordDto) {
        return null;
    }

    private UserDto from(User user){

        UserDto userDto=new UserDto();
        userDto.setEmail(user.getEmail());
         userDto.setRoles(user.getRoles());

        return userDto;


    }
}