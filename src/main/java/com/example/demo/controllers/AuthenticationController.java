package com.example.demo.controllers;

import com.example.demo.beans.SessionInfoBean;
import com.example.demo.dto.AuthRequest;
import com.example.demo.exceptions.user.UserAlreadyExistsException;
import com.example.demo.exceptions.user.UserInvalidEmailException;
import com.example.demo.exceptions.user.UserRequiredFieldEmptyException;
import com.example.demo.models.TokenResponse;
import com.example.demo.models.Userx;
import com.example.demo.services.JwtService;
import com.example.demo.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@Scope("session")
@RequestMapping("/auth")
public class AuthenticationController {
    private final UserService userService;
    private final JwtService jwtService;

    @Autowired
    public AuthenticationController(UserService userService, JwtService jwtService) {
        this.jwtService = jwtService;
        this.userService = userService;
    }

    @PostMapping("/addNewUser")
    public ResponseEntity<?> addNewUser(@RequestBody Userx user) {
        try {
            Userx userToBeAdded = userService.registerUser(user);
            return ResponseEntity.ok(userToBeAdded);
        } catch (UserAlreadyExistsException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (UserInvalidEmailException | UserRequiredFieldEmptyException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("An unexpected error occurred");
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticateAndGetToken(@RequestBody AuthRequest authRequest) {
        try {
            Userx user = userService.allowLogin(authRequest.getName(), authRequest.getPassword());
            if (user != null) {
                String token = jwtService.generateToken(user);
                TokenResponse response = new TokenResponse(token);
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Authentication failed: Invalid username or password");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred during the login process");
        }
    }


}
