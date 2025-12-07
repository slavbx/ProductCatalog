package org.slavbx.productcatalog.security;

import org.slavbx.auditstarter.model.UserProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SecurityUserProvider implements UserProvider {

    private final AuthenticationService authService;

    @Autowired
    public SecurityUserProvider(AuthenticationService authService) {
        this.authService = authService;
    }

    @Override
    public String getCurrentUserEmail() {
        return authService.getCurrentUser() != null ?
                authService.getCurrentUser().getEmail() : "unknown";
    }

    @Override
    public String getCurrentUsername() {
        return authService.getCurrentUser() != null ?
                authService.getCurrentUser().getName() : "unknown";
    }
}
