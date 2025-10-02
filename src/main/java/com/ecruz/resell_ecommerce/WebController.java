package com.ecruz.resell_ecommerce;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Web Controller for serving HTML templates
 * Updated to work with Spring Security - removed .html extensions
 * Spring Security handles authentication/authorization for these routes
 */
@Controller
public class WebController {

    @GetMapping("/")
    public String index() {
        return "index";
    }
    
    // Login page - redirect to home if already authenticated
    @GetMapping("/login")
    public String login() {
        if (isAuthenticated()) {
            return "redirect:/";
        }
        return "login";
    }
    
    // Register page - redirect to home if already authenticated
    @GetMapping("/register")
    public String register() {
        if (isAuthenticated()) {
            return "redirect:/";
        }
        return "register";
    }
    
    @GetMapping("/products")
    public String products() {
        return "product";
    }
    
    // Requires authentication (configured in SecurityConfig)
    @GetMapping("/cart")
    public String cart() {
        return "cart";
    }
    
    // Logout redirect - Spring Security handles actual logout at /logout
    @GetMapping("/logout")
    public String logout() {
        return "redirect:/login";
    }
    
    // ADMIN-only page (configured in SecurityConfig)
    @GetMapping("/add")
    public String add() {
        return "add";
    }
    
    // Requires authentication
    @GetMapping("/user")
    public String user() {
        return "user";
    }
    
    // ADMIN-only page (configured in SecurityConfig)
    @GetMapping("/edit")
    public String edit() {
        return "edit";
    }
    
    // Product details page - accessible to all
    @GetMapping("/product")
    public String product() {
        return "product";
    }
    
    // Helper method to check if user is authenticated
    private boolean isAuthenticated() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth != null && auth.isAuthenticated() && !auth.getName().equals("anonymousUser");
    }
}