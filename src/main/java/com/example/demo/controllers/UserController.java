package com.example.demo.controllers;

import com.example.demo.dto.AuthRequest;
import com.example.demo.models.TokenResponse;
import com.example.demo.models.UserInfo;
import com.example.demo.services.JwtService;
import com.example.demo.services.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/auth")
public class UserController {
    private final UserInfoService service;
    private final JwtService jwtService;
    @Autowired
    public UserController(UserInfoService service, JwtService jwtService) {
        this.jwtService = jwtService;
        this.service = service;
    }

    @PostMapping("/addNewUser")
    public String addNewUser(@RequestBody UserInfo userInfo) {
        return service.addUser(userInfo);
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticateAndGetToken(@RequestBody AuthRequest authRequest) {
        UserInfo user = service.allowLogin(authRequest.getName(), authRequest.getPassword());
        if (user != null) {
            String token = jwtService.generateToken(user);
            TokenResponse response = new TokenResponse(token);
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.ok(new TokenResponse("null"));
    }
}
