package com.lecture.coordinator.ui.controllers;

import com.lecture.coordinator.model.Userx;
import com.lecture.coordinator.services.UserService;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Controller for the user list view.
 * <p>
 * This class is part of the skeleton project provided for students of the
 * course "Software Architecture" offered by Innsbruck University.
 */
@Component
@Scope("view")
public class UserListController {
    protected final UserService userService;

    public UserListController(UserService userService) {
        this.userService = userService;
    }

    public Iterable<Userx> getUsers() {
        return userService.getAllUsers();
    }
}
