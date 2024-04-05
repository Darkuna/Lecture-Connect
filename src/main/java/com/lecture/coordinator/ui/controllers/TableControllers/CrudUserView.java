package com.lecture.coordinator.ui.controllers.TableControllers;

import com.lecture.coordinator.exceptions.user.UserAlreadyExistsException;
import com.lecture.coordinator.exceptions.user.UserInvalidEmailException;
import com.lecture.coordinator.exceptions.user.UserRequiredFieldEmptyException;
import com.lecture.coordinator.model.Userx;
import com.lecture.coordinator.model.UserxRole;
import com.lecture.coordinator.services.UserService;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
@Scope("view")
public class CrudUserView implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private List<Userx> users;

    private Userx selectedUser;

    private List<Userx> selectedUsers;

    private final UserService userService;

    private List<String> selectedRoles;

    public CrudUserView(UserService userxService) {
        this.userService = userxService;
    }

    @PostConstruct
    private void init() {
        this.users = userService.getAllUsers();
        this.selectedUsers = new ArrayList<>();
        this.selectedUser = null;
    }

    public List<Userx> getUsers() {
        return users;
    }

    public Userx getSelectedUser() {
        return selectedUser;
    }

    public void setSelectedUser(Userx selectedUser) {
        this.selectedUser = selectedUser;
    }

    public List<Userx> getSelectedUsers() {
        return selectedUsers;
    }

    public void setSelectedUsers(List<Userx> selectedUsers) {
        this.selectedUsers = selectedUsers;
    }

    public void openNew() {
        this.selectedUser = new Userx();
    }

    public void doSaveUser() throws UserAlreadyExistsException, UserRequiredFieldEmptyException, UserInvalidEmailException {
        selectedUser = this.userService.saveUser(selectedUser);
        users.add(this.selectedUser);
        init();

    }

    public void doDeleteUser() {
        this.userService.deleteUser(selectedUser);
        this.users.remove(selectedUser);
        selectedUser = null;

    }

    public String getDeleteButtonMessage() {
        if (hasSelectedUsers()) {
            int size = this.selectedUsers.size();
            return size > 1 ? size + " users selected" : "1 user selected";
        }

        return "Delete";
    }

    public boolean hasSelectedUsers() {
        return this.selectedUsers != null && !this.selectedUsers.isEmpty();
    }

    public void deleteSelectedUsers() {
        userService.deleteMultipleUser(selectedUsers);
        this.users.removeAll(this.selectedUsers);
        this.selectedUsers = null;
    }

    public List<String> getSelectedRoles() {
        return selectedRoles;
    }

    public void setSelectedRoles(List<String> selectedRoles) {
        this.selectedRoles = selectedRoles;
    }
}
