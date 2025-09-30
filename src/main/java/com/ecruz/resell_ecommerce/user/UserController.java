package com.ecruz.resell_ecommerce.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {
	private final UserService userService;

	@Autowired
	public UserController(UserService userService) {
		this.userService = userService;
	}

	@PostMapping("/register")
	public ResponseEntity<String> register(@RequestBody User user) {
		try {
			userService.createUser(user);
			return ResponseEntity.ok("User registered successfully");
		} catch (RuntimeException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
	}

//    REMOVED: Manual login endpoint - Spring Security now handles form-based authentication
//    Login is now handled by Spring Security's /login endpoint with form submission

    @PostMapping("/update")
    public ResponseEntity<String> updateUser(@RequestBody Map<String, Object> request) {
        try {
            Long userId = Long.valueOf(request.get("id").toString());
            String currentPassword = request.get("currentPassword").toString();
            String newPassword = request.get("newPassword").toString();
            
            userService.updatePassword(userId, currentPassword, newPassword);
            return ResponseEntity.ok("Password updated successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Update failed");
        }
    }
}
