package com.example.lecture_connect.controllers;

import com.example.lecture_connect.LoginMessage;
import com.example.lecture_connect.dto.LoginDTO;
import com.example.lecture_connect.dto.UserxDTO;
import com.example.lecture_connect.services.UserxService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UserxController {
    private final UserxService userxService;

    public UserxController(UserxService employeeService) {
        this.userxService = employeeService;
    }


    @PostMapping(path = "/api/save")
    public ResponseEntity<String> saveUser(@RequestBody UserxDTO userxDTO){
        return ResponseEntity.ok().body(userxService.adduserx(userxDTO));
    }

    @PostMapping(path = "/api/login")
    public ResponseEntity<LoginMessage> loginUser(@RequestBody LoginDTO loginDTO) {
        LoginMessage loginResponse = userxService.loginuserx(loginDTO);
        return ResponseEntity.ok(loginResponse);
    }

    @GetMapping("/api/allGet")
    public ResponseEntity<String> getAllUsers() {
        return ResponseEntity.ok(userxService.getAllUsers().toString());
    }
}
