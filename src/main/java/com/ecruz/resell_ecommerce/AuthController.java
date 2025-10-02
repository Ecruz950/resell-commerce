package com.ecruz.resell_ecommerce;

import com.ecruz.resell_ecommerce.user.User;
import com.ecruz.resell_ecommerce.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * REST Controller for providing authentication status to JavaScript frontend
 * This allows client-side code to check if user is logged in and their role
 * without relying on session storage or cookies
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;

    @Autowired
    public AuthController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Endpoint that returns current user's authentication status
     * Used by JavaScript to conditionally show/hide elements based on auth state
     * 
     * @return Map containing authentication info:
     *         - authenticated: boolean (true if logged in)
     *         - username: string (if authenticated)
     *         - isAdmin: boolean (true if user has ADMIN role)
     */
    @GetMapping("/status")
    public Map<String, Object> getAuthStatus() {
        // Get current authentication from Spring Security context
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Map<String, Object> response = new HashMap<>();
        
        // Check if user is authenticated (not null, authenticated, and not anonymous)
        if (auth != null && auth.isAuthenticated() && !auth.getName().equals("anonymousUser")) {
            response.put("authenticated", true);
            response.put("username", auth.getName());
            // Check if user has ADMIN role by examining authorities
            response.put("isAdmin", auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN")));
            // Add user ID by looking up the user in the database
            response.put("userId", getUserId(auth.getName()));
        } else {
            response.put("authenticated", false);
        }
        
        return response;
    }

    /**
     * Helper method to get user ID from username
     */
    private Long getUserId(String username) {
        User user = userService.getUserByUsername(username);
        return user != null ? user.getId() : null;
    }
}