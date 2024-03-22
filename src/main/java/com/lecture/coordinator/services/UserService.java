package com.lecture.coordinator.services;

import com.lecture.coordinator.model.Userx;
import com.lecture.coordinator.exceptions.user.UserAlreadyExistsException;
import com.lecture.coordinator.exceptions.user.UserInvalidEmailException;
import com.lecture.coordinator.exceptions.user.UserRequiredFieldEmptyException;
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
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

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

    private List<Userx> streamToList(Iterable<Userx> userStream){
        return  StreamSupport.stream(userStream.spliterator(), false)
                .collect(Collectors.toList());
    }


    @PreAuthorize("hasAuthority('ADMIN')")
    public List<Userx> getAllUsers() {
        return streamToList(userRepository.findAll());
    }

    @PreAuthorize("hasAuthority('ADMIN') or principal.username eq #username")
    public Optional<Userx> loadUser(String username) {
        return userRepository.findById(username);
    }

    public boolean existsUser(String username) {
        return userRepository.findById(username).isPresent();
    }

    private void checkUserRequirements(Userx user)
            throws UserRequiredFieldEmptyException, UserAlreadyExistsException, UserInvalidEmailException {

        if (user.getUsername() == null || user.getUsername().isBlank()) {
            throw new UserRequiredFieldEmptyException("username");
        }

        if (existsUser(user.getUsername())) {
            throw new UserAlreadyExistsException("already in use");
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

    @PreAuthorize("hasAuthority('ADMIN')")
    public void deleteUser(Userx user) {
        userRepository.delete(user);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    public void deleteMultipleUser(List<Userx> users) {
        for(Userx u : users) {
            deleteUser(u);
        }
    }

    public Userx getAuthenticatedUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Optional<Userx> user = userRepository.findById(auth.getName());
        return user.orElse(null);
    }

}
