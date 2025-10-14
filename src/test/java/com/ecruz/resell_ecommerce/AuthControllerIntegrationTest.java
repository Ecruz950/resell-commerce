package com.ecruz.resell_ecommerce;

import com.ecruz.resell_ecommerce.user.User;
import com.ecruz.resell_ecommerce.user.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(value = AuthController.class, excludeAutoConfiguration = {org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration.class})
class AuthControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    /**
     * Test that GET /api/auth/status returns correct authentication info for regular user
     * Verifies authenticated user gets proper status with USER role (isAdmin=false)
     */
    @Test
    @WithMockUser(username = "testuser", roles = "USER")
    void getAuthStatus_AuthenticatedUser() throws Exception {
        User mockUser = new User();
        mockUser.setId(1L);
        mockUser.setUsername("testuser");
        when(userService.getUserByUsername("testuser")).thenReturn(mockUser);

        mockMvc.perform(get("/api/auth/status"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.authenticated").value(true))
                .andExpect(jsonPath("$.username").value("testuser"))
                .andExpect(jsonPath("$.isAdmin").value(false))
                .andExpect(jsonPath("$.userId").value(1));
    }

    /**
     * Test that GET /api/auth/status returns correct authentication info for admin user
     * Verifies admin user gets proper status with ADMIN role (isAdmin=true)
     */
    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void getAuthStatus_AdminUser() throws Exception {
        User mockAdmin = new User();
        mockAdmin.setId(2L);
        mockAdmin.setUsername("admin");
        when(userService.getUserByUsername("admin")).thenReturn(mockAdmin);

        mockMvc.perform(get("/api/auth/status"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.authenticated").value(true))
                .andExpect(jsonPath("$.username").value("admin"))
                .andExpect(jsonPath("$.isAdmin").value(true))
                .andExpect(jsonPath("$.userId").value(2));
    }

    /**
     * Test that GET /api/auth/status returns correct info for unauthenticated user
     * Verifies anonymous users get authenticated=false with no username, admin status, or userId
     */
    @Test
    void getAuthStatus_UnauthenticatedUser() throws Exception {
        mockMvc.perform(get("/api/auth/status"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.authenticated").value(false))
                .andExpect(jsonPath("$.username").doesNotExist())
                .andExpect(jsonPath("$.isAdmin").doesNotExist())
                .andExpect(jsonPath("$.userId").doesNotExist());
    }
}