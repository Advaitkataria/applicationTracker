package org.example.applicationtracker.controller;

import org.example.applicationtracker.model.User;
import org.example.applicationtracker.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;
    @Autowired
    public AuthController(AuthService authService){
        this.authService=authService;
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody User user){
        return new ResponseEntity<>(authService.register(user), HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String,String>> login(@RequestBody Map<String,String> credentials){
            String email = credentials.get("email");
            String password = credentials.get("password");
            return new ResponseEntity<>(authService.login(email,password),HttpStatus.OK);
    }

    @PostMapping("/admin-register")
    public ResponseEntity<String> registerAdmin(@RequestBody User user){
        return new ResponseEntity<>(authService.registerAdmin(user),HttpStatus.CREATED);
    }


}
