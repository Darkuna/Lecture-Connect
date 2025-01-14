package at.uibk.leco.service;

import at.uibk.leco.exceptions.user.UserAlreadyExistsException;
import at.uibk.leco.exceptions.user.UserInvalidEmailException;
import at.uibk.leco.exceptions.user.UserRequiredFieldEmptyException;
import at.uibk.leco.models.Userx;
import at.uibk.leco.models.UserxRole;
import at.uibk.leco.repositories.UserRepository;
import at.uibk.leco.services.UserService;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
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
    @Autowired
    private UserRepository userRepository;

    @DirtiesContext
    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    public void testDeleteUser() {
        int numberOfUsers = userRepository.findAll().size();
        String username = "user1";
        Userx adminUser = userService.loadUser("admin").get();
        Assertions.assertNotNull(adminUser, "Admin user could not be loaded from test data source");
        Userx toBeDeletedUser = userService.loadUser(username).get();
        Assertions.assertNotNull(toBeDeletedUser,
                "User \"" + username + "\" could not be loaded from test data source");

        userService.deleteUser(toBeDeletedUser);

        Assertions.assertEquals(numberOfUsers-1, Lists.newArrayList(userService.getAllUsers()).size(),
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
    @WithMockUser(username = "user1", authorities = {"USER"})
    public void testAuthorizedLoadUser() {
        String username = "user1";
        Userx user = userService.loadUser(username).get();
        Assertions.assertEquals(username, user.getUsername(),
                "Call to userService.loadUser returned wrong user");
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