package com.example.userauthenticationservice.services;

import com.example.userauthenticationservice.exceptions.InvalidCredentialsException;
import com.example.userauthenticationservice.models.Session;
import com.example.userauthenticationservice.models.SessionState;
import com.example.userauthenticationservice.models.User;
import com.example.userauthenticationservice.repos.SessionRepo;
import com.example.userauthenticationservice.repos.UserRepo;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.MacAlgorithm;
import org.antlr.v4.runtime.misc.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.http.HttpHeaders;


import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class AuthService implements IAuthService {

@Autowired
private UserRepo userRepo;

@Autowired
private SessionRepo sessionRepo;

@Autowired
private BCryptPasswordEncoder bCryptPasswordEncoder;

public User signup(String email, String password) {


    Optional<User> optionalUser= userRepo.findByEmail(email);

    if(optionalUser.isPresent()) {
       return null;

    }

User user = new User();
    user.setEmail(email);
    user.setPassword(bCryptPasswordEncoder.encode(password));
    userRepo.save(user);
    return user;

}

public Pair<User,MultiValueMap<String,String>> login(String email, String password) {

    Optional<User> optionalUser= userRepo.findByEmail(email);

    if (optionalUser.isEmpty() || !bCryptPasswordEncoder.matches(password, optionalUser.get().getPassword())) {
        throw new InvalidCredentialsException("Invalid email or password");
    }

//    if(optionalUser.isEmpty()) {
//        return null;
//
//    }
//    User user = optionalUser.get();
//if(!bCryptPasswordEncoder.matches(password, user.getPassword())) {
//    return null;
//
//}

//    String message = "{\n" +
//            "    \"email\": \"git@gmail.com\",\n" +
//            "    \"roles\": [\"admin\", \"user\"],\n" +
//            "    \"expirationDate\": \"2025-03-05\"\n" +
//            "}";

//    String message = "{\n" +
//            " \"email": "anurag@scaler.com",\n" +
//            " \"roles": [\n" +
//            " \"instructor",\n" +
//            " \"buddy"\n" +
//            " ],\n" +
//            " \"expirationDate": "25thJuly2024"\n" +
//            "}";

    Map<String,Object>claims=new HashMap<>();
    claims.put("email",optionalUser.get().getEmail());
    claims.put("role",optionalUser.get().getRoles());
    claims.put("user_id",optionalUser.get().getId());
    long nowInMillis = System.currentTimeMillis();
    claims.put("created_at",nowInMillis);
    claims.put("exp_at",nowInMillis+1000000);



//byte[]content=message.getBytes(StandardCharsets.UTF_8);
    MacAlgorithm algorithm=Jwts.SIG.HS256;
    SecretKey secretKey= algorithm.key().build();
String token = Jwts.builder().claims(claims).signWith(secretKey).compact();

    MultiValueMap<String,String> headers=new LinkedMultiValueMap<>();
    headers.add(HttpHeaders.SET_COOKIE,token);


    Session session=new Session();
    session.setSessionState(SessionState.ACTIVE);
    session.setUser(optionalUser.get());
    session.setToken(token);
    sessionRepo.save(session);
return new Pair<User,MultiValueMap<String,String>>(optionalUser.get(),headers);



//TOKEN VALIDATION

}


}
