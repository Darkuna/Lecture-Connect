package com.lecture.coordinator.ui.controllers;

import com.lecture.coordinator.exceptions.user.UserAlreadyExistsException;
import com.lecture.coordinator.exceptions.user.UserInvalidEmailException;
import com.lecture.coordinator.exceptions.user.UserRequiredFieldEmptyException;
import com.lecture.coordinator.model.UserxRole;
import com.lecture.coordinator.model.Userx;
import com.lecture.coordinator.services.UserService;
import org.apache.catalina.User;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Controller for the user detail view.
 * <p>
 * This class is part of the skeleton project provided for students of the
 * course "Software Architecture" offered by Innsbruck University.
 */
@Component
@Scope("view")
public class UserDetailController {

    private List<String> selectedRoles;
    private final UserService userService;
    private Userx singleUser;

    public UserDetailController(UserService userService) {
        this.userService = userService;
    }

    @PostConstruct
    public void init() {
        this.singleUser = null;
    }

    public void doReloadUser() {
        this.singleUser = userService.loadUser(singleUser.getUsername()).orElse(null);
    }

    public void doSaveUser() throws UserAlreadyExistsException, UserRequiredFieldEmptyException, UserInvalidEmailException {
        this.singleUser = this.userService.saveUser(this.singleUser);
        init();
    }


    public void createUser() {
        Userx user = new Userx();

        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext()
                .getRequest();

        user.setFirstName(request.getParameter("createForm:firstNameCreation"));
        user.setLastName(request.getParameter("createForm:lastNameCreation"));
        user.setEmail(request.getParameter("createForm:mailCreation"));
        user.setId(request.getParameter("createForm:usernameCreation"));
        user.setPassword(request.getParameter("createForm:passwordCreation"));
        user.setEnabled(true);

        Set<UserxRole> roles = new HashSet<>();
        if (selectedRoles != null) {
            for (String role : selectedRoles) {
                if (role.equals("USER"))
                    roles.add(UserxRole.ADMIN);
                if (role.equals("USER"))
                    roles.add(UserxRole.USER);
            }
        }
        user.setRoles(roles);
        try {
            user = this.userService.saveUser(user);
            init();
        } catch (UserAlreadyExistsException | UserRequiredFieldEmptyException | UserInvalidEmailException e) {
            ControllerUtils.addMessage(FacesMessage.SEVERITY_FATAL, "Couldn't create user", e.getMessage());
        }
    }

    public List<String> getSelectedRoles() {
        return selectedRoles;
    }

    public void setSelectedRoles(List<String> selectedRoles) {
        this.selectedRoles = selectedRoles;
    }

    public Userx getSingleUser() {
        return singleUser;
    }

    public void setSingleUser(Userx singleUser) {
        this.singleUser = singleUser;
    }
}

