package com.example.demo.beans;

import com.example.demo.models.Userx;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import com.example.demo.services.UserService;

import java.io.Serializable;
import java.util.Optional;

@Component
@Scope("session")
public class SessionInfoBean implements Serializable {
    @Autowired
    private final UserService userService;

    public SessionInfoBean(UserService userService) {
        this.userService = userService;

    }
    /**
     * Attribute to cache the current user.
     */
    private Userx currentUser;

    /**
     * @return the currently logged on user, null if no user is authenticated for this session.
     */
    public Userx getCurrentUser() {
        if (currentUser == null) {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth != null && auth.isAuthenticated() && !(auth instanceof AnonymousAuthenticationToken)) {
                String username = auth.getName();
                Optional<Userx> user = userService.loadUser(username);
                user.ifPresent(userx -> currentUser = userx);
            }
        }
        return currentUser;
    }
    /**
     * @return the username of the user for this session, empty string if no user has been authenticated for this session.
     */
    public String getCurrentUserName() {
        if (!isLoggedIn()) {
            return "";
        }
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth.getName();
    }

    /**
     * @return the roles of the user for this session as space-separated list, empty string if no user has been authenticated for this session-
     */
    public String getCurrentUserRoles() {
        if (!isLoggedIn()) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        for (GrantedAuthority role : auth.getAuthorities()) {
            sb.append(role.getAuthority());
            sb.append(' ');
        }
        return sb.toString().trim();
    }

    /**
     * Checks if a user is authenticated for this session.
     * @return true if a non-anonymous user has been authenticated, false otherwise
     */
    public boolean isLoggedIn() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            return auth.isAuthenticated() && !auth.getName().equals("anonymousUser");
        } else {
            return false;
        }
    }

    /**
     * Checks if the user for this session has the given role (c.f.
     * {@link UserxRole}).
     *
     * @param role the role to check for as string
     * @return true if a user is authenticated and the current user has the
     * given role, false otherwise
     */
    public boolean hasRole(String role) {
        if (role == null || role.isEmpty() || !isLoggedIn()) {
            return false;
        }
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        for (GrantedAuthority ga : auth.getAuthorities()) {
            if (role.equals(ga.getAuthority())) {
                return true;
            }
        }
        return false;
    }

    public void setCurrentUser(Userx user) {
        this.currentUser = user;
    }
}
