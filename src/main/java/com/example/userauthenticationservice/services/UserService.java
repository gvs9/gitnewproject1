package com.example.userauthenticationservice.services;

import com.example.userauthenticationservice.models.User;
import com.example.userauthenticationservice.repos.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {


@Autowired
    private UserRepo userRepo;

    public User getUser(Long id) {

        Optional<User> userOptional = userRepo.findById(id);
        if(userOptional.isPresent()) {
            return userOptional.get();
        }
return null;
    }
}

