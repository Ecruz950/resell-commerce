package com.ecruz.resell_ecommerce.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

	private final UserRepository userRepository;

	@Autowired
	public UserService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	public User getUserById(Long id) {
		return userRepository.findById(id).orElse(null);
	}

	public User getUserByUsername(String username) {
		return userRepository.findByUsername(username).orElse(null);
	}

	public User createUser(User user) {
		return userRepository.save(user);
	}

	public User updateUser(User user) {
		User existingUser = userRepository.findById(user.getId()).orElse(null);
		if (existingUser != null) {
			existingUser.setUsername(user.getUsername());
			existingUser.setPassword(user.getPassword());
			existingUser.setRole(user.getRole());
			return userRepository.save(existingUser);
		}
		return null;
	}

	public void deleteUser(Long id) {
		userRepository.deleteById(id);
	}

	public boolean authenticate(User user) {
		User existingUser = userRepository.findByUsername(user.getUsername()).orElse(null);
		if (existingUser != null) {
			return existingUser.getPassword().equals(user.getPassword());
		}
		return false;
	}
}
