package com.example.demo.service;

import com.example.demo.exceptions.user.UserAlreadyExistsException;
import com.example.demo.exceptions.user.UserInvalidEmailException;
import com.example.demo.exceptions.user.UserRequiredFieldEmptyException;
import com.example.demo.models.Userx;
import com.example.demo.models.UserxRole;
import com.example.demo.services.UserService;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.internal.util.collections.Sets;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.Optional;

/**
 * Some very basic tests for {@link UserService}.
 * <p>
 * This class is part of the skeleton project provided for students of the
 * courses "Software Architecture" and "Software Engineering" offered by the
 * University of Innsbruck.
 */
@SpringBootTest
@WebAppConfiguration
@ActiveProfiles("dev")
public class UserServiceTest {

    @Autowired
    UserService userService;

    @DirtiesContext
    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    public void testDeleteUser() {
        String username = "user1";
        Userx adminUser = userService.loadUser("admin").get();
        Assertions.assertNotNull(adminUser, "Admin user could not be loaded from test data source");
        Userx toBeDeletedUser = userService.loadUser(username).get();
        Assertions.assertNotNull(toBeDeletedUser,
                "User \"" + username + "\" could not be loaded from test data source");

        userService.deleteUser(toBeDeletedUser);

        Assertions.assertEquals(13, Lists.newArrayList(userService.getAllUsers()).size(),
                "No user has been deleted after calling UserService.deleteUser");
        Optional<Userx> deletedUser = userService.loadUser(username);
        Assertions.assertTrue(deletedUser.isEmpty(), "Deleted User \"" + username
                + "\" could still be loaded from test data source via UserService.loadUser");

        for (Userx remainingUser : userService.getAllUsers()) {
            Assertions.assertNotEquals(toBeDeletedUser.getUsername(), remainingUser.getUsername(),
                    "Deleted User \""
                            + username
                            + "\" could still be loaded from test data source via UserService.getAllUsers");
        }
    }

    @DirtiesContext
    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    public void testUpdateUser()
            throws UserAlreadyExistsException, UserRequiredFieldEmptyException, UserInvalidEmailException {
        String username = "user1";
        Userx adminUser = userService.loadUser("admin").get();
        Assertions.assertNotNull(adminUser, "Admin user could not be loaded from test data source");
        Userx toBeSavedUser = userService.loadUser(username).get();
        Assertions.assertNotNull(toBeSavedUser,
                "User \"" + username + "\" could not be loaded from test data source");

        Assertions.assertNull(toBeSavedUser.getUpdateUser(),
                "User \"" + username + "\" has a updateUser defined");
        Assertions.assertNull(toBeSavedUser.getUpdateDate(),
                "User \"" + username + "\" has a updateDate defined");

        toBeSavedUser.setEmail("changed-email@whatever.wherever");
        userService.saveUser(toBeSavedUser);

        Userx freshlyLoadedUser = userService.loadUser("user1").get();
        Assertions.assertNotNull(freshlyLoadedUser,
                "User \"" + username
                        + "\" could not be loaded from test data source after being saved");
        Assertions.assertNotNull(freshlyLoadedUser.getUpdateUser(),
                "User \"" + username + "\" does not have a updateUser defined after being saved");
        Assertions.assertEquals(adminUser, freshlyLoadedUser.getUpdateUser(),
                "User \"" + username + "\" has wrong updateUser set");
        Assertions.assertNotNull(freshlyLoadedUser.getUpdateDate(),
                "User \"" + username + "\" does not have a updateDate defined after being saved");
        Assertions.assertEquals("changed-email@whatever.wherever", freshlyLoadedUser.getEmail(),
                "User \"" + username
                        + "\" does not have a the correct email attribute stored being saved");
    }

    @Autowired
    PasswordEncoder passwordEncoder;

    @DirtiesContext
    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    public void testCreateUser()
            throws UserAlreadyExistsException, UserRequiredFieldEmptyException, UserInvalidEmailException {
        Userx adminUser = userService.loadUser("admin").get();
        Assertions.assertNotNull(adminUser, "Admin user could not be loaded from test data source");

        String username = "newuser";
        String password = "passwd";
        String fName = "New";
        String lName = "User";
        String email = "new-email@whatever.wherever";
        Userx toBeCreatedUser = new Userx();
        toBeCreatedUser.setUsername(username);
        toBeCreatedUser.setPassword(password);
        toBeCreatedUser.setEnabled(true);
        toBeCreatedUser.setFirstName(fName);
        toBeCreatedUser.setLastName(lName);
        toBeCreatedUser.setEmail(email);
        toBeCreatedUser.setRoles(Sets.newSet(UserxRole.USER));
        userService.saveUser(toBeCreatedUser);

        Userx freshlyCreatedUser = userService.loadUser(username).get();
        Assertions.assertNotNull(freshlyCreatedUser,
                "New user could not be loaded from test data source after being saved");
        Assertions.assertEquals(username, freshlyCreatedUser.getUsername(),
                "New user could not be loaded from test data source after being saved");
        Assertions.assertTrue(passwordEncoder.matches(password, freshlyCreatedUser.getPassword()),
                "User \"" + username
                        + "\" does not have a the correct password attribute stored being saved");
        Assertions.assertEquals(fName, freshlyCreatedUser.getFirstName(),
                "User \"" + username
                        + "\" does not have a the correct firstName attribute stored being saved");
        Assertions.assertEquals(lName, freshlyCreatedUser.getLastName(),
                "User \"" + username
                        + "\" does not have a the correct lastName attribute stored being saved");
        Assertions.assertEquals(email, freshlyCreatedUser.getEmail(),
                "User \"" + username
                        + "\" does not have a the correct email attribute stored being saved");
        Assertions.assertTrue(freshlyCreatedUser.getRoles().contains(UserxRole.USER),
                "User \"" + username + "\" does not have role USER");
        Assertions.assertNotNull(freshlyCreatedUser.getCreateUser(),
                "User \"" + username + "\" does not have a createUser defined after being saved");
        Assertions.assertEquals(adminUser, freshlyCreatedUser.getCreateUser(),
                "User \"" + username + "\" has wrong createUser set");
        Assertions.assertNotNull(freshlyCreatedUser.getCreateDate(),
                "User \"" + username + "\" does not have a createDate defined after being saved");
    }

    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    public void testExceptionForEmptyUsername() {
        Assertions.assertThrows(UserRequiredFieldEmptyException.class, () -> {
            Userx adminUser = userService.loadUser("admin").get();
            Assertions.assertNotNull(adminUser, "Admin user could not be loaded from test data source");

            Userx toBeCreatedUser = new Userx();
            userService.saveUser(toBeCreatedUser);
        });
    }

    @Test
    public void testUnauthenticateddLoadUsers() {
        Assertions.assertThrows(
                org.springframework.security.authentication.AuthenticationCredentialsNotFoundException.class,
                () -> {
                    for (Userx user : userService.getAllUsers()) {
                        Assertions.fail("Call to userService.getAllUsers should not work without proper authorization");
                    }
                });
    }

    @Test
    @WithMockUser(username = "user1", authorities = {"USER"})
    public void testUnauthorizedLoadUsers() {
        Assertions.assertThrows(org.springframework.security.access.AccessDeniedException.class, () -> {
            for (Userx user : userService.getAllUsers()) {
                Assertions.fail("Call to userService.getAllUsers should not work without proper authorization");
            }
        });
    }

    @Test
    @WithMockUser(username = "user1", authorities = {"USER"})
    public void testUnauthorizedLoadUser() {
        Assertions.assertThrows(org.springframework.security.access.AccessDeniedException.class, () -> {
            Userx user = userService.loadUser("admin").get();
            Assertions.fail(
                    "Call to userService.loadUser should not work without proper authorization for other users than the authenticated one");
        });
    }

    @WithMockUser(username = "user1", authorities = {"USER"})
    public void testAuthorizedLoadUser() {
        String username = "user1";
        Userx user = userService.loadUser(username).get();
        Assertions.assertEquals(username, user.getUsername(),
                "Call to userService.loadUser returned wrong user");
    }

    @Test
    @WithMockUser(username = "user1", authorities = {"USER"})
    public void testUnauthorizedSaveUser() {
        Assertions.assertThrows(org.springframework.security.access.AccessDeniedException.class, () -> {
            String username = "user1";
            Userx user = userService.loadUser(username).get();
            Assertions.assertEquals(username, user.getUsername(),
                    "Call to userService.loadUser returned wrong user");
            userService.saveUser(user);
        });
    }

    @Test
    @WithMockUser(username = "user1", authorities = {"USER"})
    public void testUnauthorizedDeleteUser() {
        Assertions.assertThrows(org.springframework.security.access.AccessDeniedException.class, () -> {
            Userx user = userService.loadUser("user1").get();
            Assertions.assertEquals("user1", user.getUsername(),
                    "Call to userService.loadUser returned wrong user");
            userService.deleteUser(user);
        });
    }

    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    public void testRegisterUser() {
        String username = "newRegisterUser";
        Userx user = new Userx();
        user.setUsername(username);
        user.setPassword("passwd");
        user.setEmail("email@email.com");
        user.setCreateUser(user);

        try {
            userService.registerUser(user);
        } catch (Exception e) {
            Assertions.fail(e);
        }

        Assertions.assertEquals(user, userService.loadUser(username).orElse(null));
    }

    @Test
    public void testRegisterUserAlreadyExists() {
        Assertions.assertThrows(UserAlreadyExistsException.class, () -> {
            Userx user = new Userx();
            user.setUsername("admin"); // this name already exists
            user.setPassword("passwd");
            user.setEmail("email");
            user.setCreateUser(user);
            userService.registerUser(user);
        });
    }

    @Test
    public void testRegisterUserNotAllRequirementsUsername() {
        Assertions.assertThrows(UserRequiredFieldEmptyException.class, () -> {
            Userx user = new Userx();
            user.setPassword("passwd");
            user.setEmail("valid@email.com");
            user.setCreateUser(user);
            userService.registerUser(user);
        });

        Assertions.assertThrows(UserRequiredFieldEmptyException.class, () -> {
            Userx user = new Userx();
            user.setUsername("");
            user.setPassword("passwd");
            user.setEmail("valid@email.com");
            user.setCreateUser(user);
            userService.registerUser(user);
        });
    }

    @Test
    public void testRegisterUserNotAllRequirementsPassword() {
        Assertions.assertThrows(UserRequiredFieldEmptyException.class, () -> {
            Userx user = new Userx();
            user.setUsername("newRegisteredUser");
            user.setEmail("valid@email.com");
            user.setCreateUser(user);
            userService.registerUser(user);
        });

        Assertions.assertThrows(UserRequiredFieldEmptyException.class, () -> {
            Userx user = new Userx();
            user.setUsername("newRegisteredUser");
            user.setPassword("");
            user.setEmail("valid@email.com");
            user.setCreateUser(user);
            userService.registerUser(user);
        });
    }

    @Test
    public void testRegisterUserNotAllRequirementsEmail() {
        Assertions.assertThrows(UserRequiredFieldEmptyException.class, () -> {
            Userx user = new Userx();
            user.setUsername("newRegisteredUser");
            user.setPassword("passwd");
            user.setCreateUser(user);
            userService.registerUser(user);
        });

        Assertions.assertThrows(UserRequiredFieldEmptyException.class, () -> {
            Userx user = new Userx();
            user.setUsername("newRegisteredUser");
            user.setPassword("passwd");
            user.setEmail("");
            user.setCreateUser(user);
            userService.registerUser(user);
        });
    }

    @Test
    public void testRegisterUserNotValidEmail() {
        Assertions.assertThrows(UserInvalidEmailException.class, () -> {
            Userx user = new Userx();
            user.setUsername("newRegisteredUser");
            user.setPassword("passwd");
            user.setEmail("not a valid email there should be a {at-sign}");
            user.setCreateUser(user);
            userService.registerUser(user);
        });
    }
}