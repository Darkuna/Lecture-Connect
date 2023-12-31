package com.lecture.coordinator.services;

import com.lecture.coordinator.exceptions.user.UserAlreadyExistsException;
import com.lecture.coordinator.exceptions.user.UserInvalidEmailException;
import com.lecture.coordinator.exceptions.user.UserRequiredFieldEmptyException;
import com.lecture.coordinator.model.Userx;
import com.lecture.coordinator.model.UserxRole;
import com.lecture.coordinator.repositories.UserRepository;
import org.springframework.context.annotation.Scope;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashSet;
import java.util.Optional;

/**
 * Service for accessing and manipulating user data.
 * <p>
 * This class is part of the skeleton project provided for students of the
 * course "Software Architecture" offered by Innsbruck University.
 */
@Component
@Scope("application")
public class UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Returns a collection of all users.
     *
     * @return
     */
    @PreAuthorize("hasAuthority('ADMIN')")
    public Iterable<Userx> getAllUsers() {
        return userRepository.findAll();
    }

    /**
     * Loads a single user identified by its username.
     *
     * @param username the username to search for
     * @return the user with the given username
     */
    @PreAuthorize("hasAuthority('ADMIN') or principal.username eq #username")
    public Optional<Userx> loadUser(String username) {
        return userRepository.findById(username);
    }

    /**
     * Checks if there is a user with this username
     *
     * @param username the username to search for
     * @return there is a user with this username
     */
    public boolean existsUser(String username) {
        return userRepository.findById(username).isPresent();
    }

    /**
     * Checks the requirements a user has to have if it doesn't have the
     * requirements then it throws a exception
     *
     * @param user
     * @throws UserRequiredFieldEmptyException
     * @throws UserAlreadyExistsException
     */
    private void checkUserRequirements(Userx user)
            throws UserRequiredFieldEmptyException, UserAlreadyExistsException, UserInvalidEmailException {

        if (user.getUsername() == null || user.getUsername().isBlank()) {
            throw new UserRequiredFieldEmptyException("username");
        }

        if (existsUser(user.getUsername())) {
            throw new UserAlreadyExistsException();
        }

        if (user.getPassword() == null || user.getPassword().isBlank()) {
            throw new UserRequiredFieldEmptyException("password");
        }

        if (user.getEmail() == null || user.getEmail().isBlank()) {
            throw new UserRequiredFieldEmptyException("email");
        }

        if (!user.getEmail().contains("@")) {
            throw new UserInvalidEmailException("Please Enter a valid email address");
        }
    }

    /**
     * Registers a new User.
     *
     * @param user the user to save
     * @return the registerd user
     * @throws UserAlreadyExistsException
     * @throws UserRequiredFieldEmptyException
     */
    public Userx registerUser(Userx user)
            throws UserAlreadyExistsException, UserRequiredFieldEmptyException, UserInvalidEmailException {
        checkUserRequirements(user);

        user.setCreateDate(new Date());
        user.setEnabled(true);
        user.setRoles(new HashSet<>());
        user.getRoles().add(UserxRole.USER);

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        return userRepository.save(user);
    }

    /**
     * Saves the user. This method will also set {@link Userx#createDate} for new
     * entities or {@link Userx#updateDate} for updated entities. The user
     * requesting this operation will also be stored as {@link Userx#createDate}
     * or {@link Userx#updateUser} respectively.
     *
     * @param user the user to save
     * @return the updated user
     * @throws UserRequiredFieldEmptyException
     * @throws UserAlreadyExistsException
     */
    @PreAuthorize("hasAuthority('ADMIN')")
    public Userx saveUser(Userx user)
            throws UserAlreadyExistsException, UserRequiredFieldEmptyException, UserInvalidEmailException {

        if (user.isNew()) {
            checkUserRequirements(user);
            user.setPassword(passwordEncoder.encode(user.getPassword()));

            user.setCreateUser(getAuthenticatedUser());
            user.setCreateDate(new Date());
        } else {
            user.setUpdateDate(new Date());
            user.setUpdateUser(getAuthenticatedUser());
        }
        return userRepository.save(user);
    }

    /**
     * Deletes the user.
     *
     * @param user the user to delete
     */
    @PreAuthorize("hasAuthority('ADMIN')")
    public void deleteUser(Userx user) {
        userRepository.delete(user);
    }

    public Userx getAuthenticatedUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Optional<Userx> user = userRepository.findById(auth.getName());
        return user.orElse(null);
    }

}
