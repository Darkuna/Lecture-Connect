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
}
