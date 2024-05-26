package com.example.demo.controllers;

import com.example.demo.exceptions.user.UserAlreadyExistsException;
import com.example.demo.exceptions.user.UserInvalidEmailException;
import com.example.demo.exceptions.user.UserRequiredFieldEmptyException;
import com.example.demo.models.Userx;
import com.example.demo.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
@Scope("session")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<Userx>> getAllUsers() {
        List<Userx> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{username}")
    public ResponseEntity<Userx> getUserByUsername(@PathVariable String username) {
        return userService.loadUser(username)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Userx> createUser(@RequestBody Userx user) {
        try {
            Userx savedUser = userService.registerUser(user);
            return ResponseEntity.ok(savedUser);
        } catch (UserAlreadyExistsException | UserInvalidEmailException | UserRequiredFieldEmptyException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @PutMapping("/{username}")
    public ResponseEntity<Userx> updateUser(@PathVariable String username, @RequestBody Userx user) {
        try {
            if (!username.equals(user.getUsername())) {
                return ResponseEntity.badRequest().build();
            }
            Userx updatedUser = userService.saveUser(user);
            return ResponseEntity.ok(updatedUser);
        } catch (UserAlreadyExistsException | UserInvalidEmailException | UserRequiredFieldEmptyException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @DeleteMapping("/{username}")
    public ResponseEntity<Void> deleteUser(@PathVariable String username) {
        Optional<Userx> user = userService.loadUser(username);
        if (user.isPresent()) {
            userService.deleteUser(user.get());
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteMultipleUsers(@RequestBody List<Userx> users) {
        userService.deleteMultipleUser(users);
        return ResponseEntity.ok().build();
    }
}
