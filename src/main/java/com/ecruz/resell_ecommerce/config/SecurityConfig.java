package com.ecruz.resell_ecommerce.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.filter.HiddenHttpMethodFilter;

/**
 * Spring Security Configuration
 * Integrates with existing User entity and provides form-based authentication
 * Replaces manual authentication with Spring Security's robust system
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    // BCrypt strength for password encoding (4 = fast for development)
    public static final int PASSWORD_ENCODER_STRENGTH = 4;
    private final UserDetailsService jpaUserDetailsService;

    @Autowired
    public SecurityConfig(UserDetailsService jpaUserDetailsService) {
        this.jpaUserDetailsService = jpaUserDetailsService;
    }

    /**
     * Main security configuration defining:
     * - Which URLs require authentication
     * - Role-based access control
     * - Login/logout behavior
     */
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .authorizeHttpRequests(
                        auth -> auth
                                // Allow static resources without authentication
                                .requestMatchers("/css/**", "/js/**", "/images/**").permitAll()
                                // Allow index, login, register pages and registration API
                                .requestMatchers("/", "/login", "/register", "/user/register").permitAll()
                                // Allow products API for browsing (GET)
                                .requestMatchers(HttpMethod.GET, "/api/v1/products/**").permitAll()
                                // Allow products API modifications for ADMIN users
                                .requestMatchers(HttpMethod.POST, "/api/v1/products/**").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.PUT, "/api/v1/products/**").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.DELETE, "/api/v1/products/**").hasRole("ADMIN")
                                // ADMIN-only pages (role-based access control)
                                .requestMatchers("/add", "/edit").hasRole("ADMIN")
                                // Other API endpoints require authentication
                                .requestMatchers("/api/**").authenticated()
                                // All other requests require authentication
                                .anyRequest().authenticated()
                )
                // Form-based login configuration
                .formLogin(
                        form -> form
                                .loginPage("/login")  // Custom login page
                                .defaultSuccessUrl("/", true)  // Redirect to home after login
                                .failureUrl("/login?error=true")  // Redirect to login with error parameter
                                .permitAll()
                )
                // Logout configuration
                .logout(
                        logout -> logout
                                .logoutUrl("/logout")  // Logout endpoint
                                .logoutSuccessUrl("/login")  // Redirect after logout
                                .invalidateHttpSession(true)  // Clear session
                                .deleteCookies("JSESSIONID")  // Remove session cookie
                                .permitAll()
                )
                // Use custom UserDetailsService (JpaUserDetailsService)
                .userDetailsService(jpaUserDetailsService)
                // Disable CSRF for API endpoints (enabled for forms by default)
                .csrf(AbstractHttpConfigurer::disable)
                // Disable HTTP Basic auth (using form-based instead)
                .httpBasic(AbstractHttpConfigurer::disable)
                .build();
    }

    /**
     * Password encoder bean for hashing passwords
     * Used by UserService when creating users
     */
    @Bean
    BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(PASSWORD_ENCODER_STRENGTH);
    }

    /**
     * Allows HTTP methods like PUT, DELETE in forms using hidden fields
     */
    @Bean
    public HiddenHttpMethodFilter hiddenHttpMethodFilter() {
        return new HiddenHttpMethodFilter();
    }
}

