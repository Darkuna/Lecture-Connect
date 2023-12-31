package com.lecture.coordinator.ui.controllers;

import com.lecture.coordinator.exceptions.user.UserAlreadyExistsException;
import com.lecture.coordinator.exceptions.user.UserInvalidEmailException;
import com.lecture.coordinator.exceptions.user.UserRequiredFieldEmptyException;
import com.lecture.coordinator.model.Userx;
import com.lecture.coordinator.services.UserService;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.faces.application.FacesMessage;

/**
 * Controller for the user detail view.
 * <p>
 * This class is part of the skeleton project provided for students of the
 * course "Software Architecture" offered by Innsbruck University.
 */
@Component
@Scope("view")
public class UserRegisterController {
    private boolean errorMode = false;
    private String errorCause;

    private final UserService userService;

    public UserRegisterController(UserService userService) {
        this.user = new Userx();
        this.user.setCreateUser(this.user);

        this.userService = userService;
    }

    /**
     * Attribute to cache the currently displayed user
     */
    private final Userx user;


    public void doRegisterUser() {
        try {
            this.userService.registerUser(this.user);

            ControllerUtils.redirect("/login.xhtml");
        } catch (UserAlreadyExistsException e) {
            ControllerUtils.addMessage(FacesMessage.SEVERITY_ERROR, "Username not available", "This username is already available");
        } catch (UserRequiredFieldEmptyException e) {
            ControllerUtils.addMessage(FacesMessage.SEVERITY_ERROR, "Empty Field", "Please fill out all required fields");
        } catch (UserInvalidEmailException e) {
            setErrorMode(e.getMessage());
        }

    }

    public void navigateLogin() {
        // only executes if no exception was thrown
        ControllerUtils.redirect("login.xhtml");
    }

    public Userx getUser() {
        return user;
    }

    public void setErrorMode(String cause) {
        this.errorMode = true;

        this.errorCause = cause;
    }

    public boolean getErrorMode() {
        return errorMode;
    }

    public String getErrorCause() {
        return errorCause;
    }
}
