package at.uibk.leco.services;
import at.uibk.leco.exceptions.user.UserAlreadyExistsException;
import at.uibk.leco.exceptions.user.UserInvalidEmailException;
import at.uibk.leco.exceptions.user.UserRequiredFieldEmptyException;
import at.uibk.leco.models.Userx;
import at.uibk.leco.models.UserxRole;
import at.uibk.leco.repositories.UserRepository;
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
 * Service class for managing users within the application.
 * Handles user registration, deletion, and retrieval, including administrative operations.
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
     * Converts an Iterable of Userx objects to a List.
     *
     * @param userStream The iterable of Userx to be converted.
     * @return A list of Userx objects.
     */
    private List<Userx> streamToList(Iterable<Userx> userStream){
        return  StreamSupport.stream(userStream.spliterator(), false)
                .collect(Collectors.toList());
    }

    /**
     * Retrieves all users in the system. Restricted to ADMIN users only.
     *
     * @return A list of all Userx entities.
     */
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public List<Userx> getAllUsers() {
        return streamToList(userRepository.findAll());
    }

    /**
     * Loads a single user by username. Access is restricted to ADMIN users or the user themselves.
     *
     * @param username The username of the user to load.
     * @return An Optional containing the Userx if found, or empty otherwise.
     */
    @PreAuthorize("hasAuthority('ADMIN') or principal.username eq #username")
    public Optional<Userx> loadUser(String username) {
        return userRepository.findById(username);
    }

    /**
     * Checks if a user exists by their username.
     *
     * @param username The username to check for existence.
     * @return true if the user exists, false otherwise.
     */
    public boolean existsUser(String username) {
        return userRepository.findById(username).isPresent();
    }

    /**
     * Checks the provided Userx object for necessary requirements.
     *
     * @param user The Userx object to check.
     * @throws UserRequiredFieldEmptyException if a required field is empty.
     * @throws UserAlreadyExistsException if the user already exists.
     * @throws UserInvalidEmailException if the email provided is invalid.
     */
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

    /**
     * Registers a new user in the system.
     *
     * @param user The Userx object representing the new user.
     * @return The saved Userx entity.
     * @throws UserAlreadyExistsException if the user already exists.
     * @throws UserRequiredFieldEmptyException if a required field is empty.
     * @throws UserInvalidEmailException if the email provided is invalid.
     */
    public Userx registerUser(Userx user)
            throws UserAlreadyExistsException, UserRequiredFieldEmptyException, UserInvalidEmailException {
        checkUserRequirements(user);
        Userx admin = userRepository.findByUsername("admin").get();
        user.setCreateDate(new Date());
        user.setEnabled(true);
        user.setRoles(new HashSet<>());
        user.getRoles().add(UserxRole.USER);
        user.setCreateUser(admin);
        user.setUpdateUser(admin);

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        return userRepository.save(user);
    }

    /**
     * Saves a user in the system. If the user is new, registers them; otherwise updates the existing user.
     * Restricted to ADMIN users only.
     *
     * @param user The Userx object to save.
     * @return The saved Userx entity.
     * @throws UserAlreadyExistsException if the user already exists (for new user registrations).
     * @throws UserRequiredFieldEmptyException if a required field is empty.
     * @throws UserInvalidEmailException if the email provided is invalid.
     */
    @PreAuthorize("hasAuthority('ADMIN')")
    public Userx saveUser(Userx user)
            throws UserAlreadyExistsException, UserRequiredFieldEmptyException, UserInvalidEmailException {
            Userx admin = userRepository.findByUsername("admin").get();
        if (user.isNew()) {
            checkUserRequirements(user);
            user.setPassword(passwordEncoder.encode(user.getPassword()));

            user.setCreateUser(admin);
            user.setCreateDate(new Date());
        } else {
            user.setUpdateDate(new Date());
            user.setUpdateUser(admin);
        }
        return userRepository.save(user);
    }

    /**
     * Deletes a specified user from the system. Restricted to ADMIN users only.
     *
     * @param user The Userx entity to delete.
     */
    @PreAuthorize("hasAuthority('ADMIN')")
    public void deleteUser(Userx user) {
        userRepository.delete(user);
    }

    /**
     * Deletes multiple users from the system. Restricted to ADMIN users only.
     *
     * @param users The list of Userx entities to delete.
     */
    @PreAuthorize("hasAuthority('ADMIN')")
    public void deleteMultipleUser(List<Userx> users) {
        for(Userx u : users) {
            deleteUser(u);
        }
    }

    public Userx allowLogin(String name, String password){
        Optional<Userx> user = userRepository.findByUsername(name);
        if(user.isPresent() && passwordEncoder.matches(password, user.get().getPassword())){
            return user.get();
        }
        return null;
    }

}
