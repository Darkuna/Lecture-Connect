package com.lecture.coordinator.ui.controllers;

import com.lecture.coordinator.model.Userx;
import com.lecture.coordinator.services.UserService;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@Scope("view")
public class UserListController {
    protected final UserService userService;

    List<Userx> selectedUsers;

    public UserListController(UserService userService) {
        this.userService = userService;
        this.selectedUsers = new ArrayList<>();
    }

    public Iterable<Userx> getUsers() {
        return userService.getAllUsers();
    }

    public List<Userx> getSelectedUsers() {
        return selectedUsers;
    }

    public void setSelectedUsers(List<Userx> selectedUsers) {
        this.selectedUsers = selectedUsers;
    }

    public Userx getFirstUser(){
        return selectedUsers.get(0);
    }
}
