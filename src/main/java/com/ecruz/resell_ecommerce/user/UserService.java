package com.ecruz.resell_ecommerce.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

	private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

	@Autowired
	public UserService(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
		this.userRepository = userRepository;
	}

	public User getUserById(Long id) {
		return userRepository.findById(id).orElse(null);
	}

	public User getUserByUsername(String username) {
		return userRepository.findByUsername(username).orElse(null);
	}

    public User createUser(User user) {
        // System.out.println("Does username exist? " + userRepository.findByUsername(user.getUsername().toLowerCase()).isPresent());
        System.out.println("Does email exist? " + userRepository.findByEmail(user.getEmail().toLowerCase()).isPresent());
        if (userRepository.findByUsername(user.getUsername().toLowerCase()).isPresent()) {
            // logger.error("Username already exists: {}", user.getUsername());
            throw new RuntimeException("Username already exists");
        } else if (userRepository.findByEmail(user.getEmail().toLowerCase()).isPresent()) {
            // logger.error("Email already exists: {}", user.getEmail());
            throw new RuntimeException("Email already exists");
        }
        if (user.getRole() == null || user.getRole().isBlank()) {
            user.setRole("USER"); // Keep in mind Spring Security conventions uses "ROLE_USER"
        }
        String password = user.getPassword();
        // Enable password encoder in user service
        user.setUsername(user.getUsername().toLowerCase());
        user.setPassword(passwordEncoder.encode(password));

        return userRepository.save(user);
    }

	public User updatePassword(Long userId, String currentPassword, String newPassword) {
		User existingUser = userRepository.findById(userId).orElse(null);
		if (existingUser != null) {
			// Validate current password matches stored encoded password
			if (passwordEncoder.matches(currentPassword, existingUser.getPassword())) {
				// Current password is correct, update to new password
				existingUser.setPassword(passwordEncoder.encode(newPassword));
				return userRepository.save(existingUser);
			} else {
				// Current password is incorrect
				throw new RuntimeException("Current password is incorrect");
			}
		}
		throw new RuntimeException("User not found");
	}

	public void deleteUser(Long id) {
		userRepository.deleteById(id);
	}

//    REMOVED: authenticate() method - Spring Security now handles authentication
//    instead of manual password comparison in UserService

}
