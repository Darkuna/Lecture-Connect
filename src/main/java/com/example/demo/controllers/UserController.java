package com.example.demo.controllers;

import com.example.demo.dto.AuthRequest;
import com.example.demo.models.TokenResponse;
import com.example.demo.models.UserInfo;
import com.example.demo.models.Userx;
import com.example.demo.services.JwtService;
import com.example.demo.services.UserInfoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;

@CrossOrigin
@RestController
@RequestMapping("/auth")
public class UserController {
    private final UserInfoService service;
    private final JwtService jwtService;

    public UserController(UserInfoService service, JwtService jwtService) {
        this.jwtService = jwtService;
        this.service = service;
    }


    @GetMapping("/welcome")
    public String welcome() {
        return "Welcome this endpoint is not secure";
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

    @GetMapping("/user/userProfile")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public String userProfile() {
        return "Welcome to User Profile";
    }

    @GetMapping("/admin/adminProfile")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public String adminProfile() {
        return "Welcome to Admin Profile";
    }
}
