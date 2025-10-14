package com.ecruz.resell_ecommerce.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(value = UserController.class, excludeAutoConfiguration = {org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration.class})
class UserControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    /**
     * Test that POST /user/register successfully creates a new user
     * Verifies successful user registration returns 200 OK with success message
     */
    @Test
    void register_Success() throws Exception {
        User user = new User();
        user.setUsername("testuser");
        user.setEmail("test@example.com");
        user.setPassword("password123");
        user.setRole("USER");

        when(userService.createUser(any(User.class))).thenReturn(user);

        mockMvc.perform(post("/user/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isOk())
                .andExpect(content().string("User registered successfully"));
    }

    /**
     * Test that POST /user/register returns 400 Bad Request when username already exists
     * Verifies proper error handling for duplicate username registration attempts
     */
    @Test
    void register_UsernameExists_ReturnsBadRequest() throws Exception {
        User user = new User();
        user.setUsername("existinguser");
        user.setEmail("test@example.com");
        user.setPassword("password123");

        when(userService.createUser(any(User.class)))
                .thenThrow(new RuntimeException("Username already exists"));

        mockMvc.perform(post("/user/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Username already exists"));
    }

    /**
     * Test that POST /user/register returns 400 Bad Request when email already exists
     * Verifies proper error handling for duplicate email registration attempts
     */
    @Test
    void register_EmailExists_ReturnsBadRequest() throws Exception {
        User user = new User();
        user.setUsername("newuser");
        user.setEmail("existing@example.com");
        user.setPassword("password123");

        when(userService.createUser(any(User.class)))
                .thenThrow(new RuntimeException("Email already exists"));

        mockMvc.perform(post("/user/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Email already exists"));
    }

    /**
     * Test that POST /user/update successfully updates user password
     * Verifies password update returns 200 OK with success message
     */
    @Test
    void updatePassword_Success() throws Exception {
        User updatedUser = new User();
        updatedUser.setId(1L);
        
        when(userService.updatePassword(1L, "currentPass", "newPass"))
                .thenReturn(updatedUser);

        String requestBody = "{\"id\":1,\"currentPassword\":\"currentPass\",\"newPassword\":\"newPass\"}";

        mockMvc.perform(post("/user/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(content().string("Password updated successfully"));
    }

    /**
     * Test that POST /user/update returns 400 Bad Request for incorrect current password
     * Verifies proper error handling when current password validation fails
     */
    @Test
    void updatePassword_WrongCurrentPassword_ReturnsBadRequest() throws Exception {
        when(userService.updatePassword(1L, "wrongPass", "newPass"))
                .thenThrow(new RuntimeException("Current password is incorrect"));

        String requestBody = "{\"id\":1,\"currentPassword\":\"wrongPass\",\"newPassword\":\"newPass\"}";

        mockMvc.perform(post("/user/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Current password is incorrect"));
    }
}