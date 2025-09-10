package com.ecruz.resell_ecommerce.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
		userService.createUser(user);
		return ResponseEntity.ok("User registered successfully");
	}

	@PostMapping("/login")
	public ResponseEntity<String> login(@RequestBody User user) {

		User userByUsername = userService.getUserByUsername(user.getUsername());

		boolean isAuthenticated = userService.authenticate(user);
		return isAuthenticated ?
				ResponseEntity.status(HttpStatus.OK)
                        // .body(String.valueOf(userByUsername.getId())) :
						.body("{\"userID\":" + userByUsername.getId() + ",\"role\":\"" + userByUsername.getRole() + "\"}") :
				ResponseEntity.status(HttpStatus.UNAUTHORIZED)
						.body("Login failed");
	}
}
