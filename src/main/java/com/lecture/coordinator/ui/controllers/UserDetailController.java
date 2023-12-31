package com.lecture.coordinator.ui.controllers;

import com.lecture.coordinator.exceptions.user.UserAlreadyExistsException;
import com.lecture.coordinator.exceptions.user.UserInvalidEmailException;
import com.lecture.coordinator.exceptions.user.UserRequiredFieldEmptyException;
import com.lecture.coordinator.model.Userx;
import com.lecture.coordinator.model.UserxRole;
import com.lecture.coordinator.services.UserService;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
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

    /**
     * Attribute to cache the currently displayed user
     */
    private Userx user;

    public UserDetailController(UserService userService) {
        this.userService = userService;
    }

    @PostConstruct
    public void init() {
        this.user = null;
    }

    /**
     * Sets the currently displayed user and reloads it form db. This user is
     * targeted by any further calls of
     * {@link #doReloadUser()}, {@link #doSaveUser()} and
     * {@link #doDeleteUser()}.
     *
     * @param user
     */
    public void setUser(Userx user) {
        this.user = user;
        doReloadUser();
    }

    /**
     * Returns the currently displayed user.
     *
     * @return
     */
    public Userx getUser() {
        return user;
    }

    /**
     * Action to force a reload of the currently displayed user.
     */
    public void doReloadUser() {
        user = userService.loadUser(user.getUsername()).orElse(null);
    }

    /**
     * Action to save the currently displayed user.
     *
     * @throws UserRequiredFieldEmptyException
     * @throws UserAlreadyExistsException
     */
    public void doSaveUser() throws UserAlreadyExistsException, UserRequiredFieldEmptyException, UserInvalidEmailException {
        user = this.userService.saveUser(user);
        init();
    }

    /**
     * Action to delete the currently displayed user.
     */
    public void doDeleteUser() {
        this.userService.deleteUser(user);
        user = null;
        init();
    }

    public void createUser() {
        user = new Userx();

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
}
