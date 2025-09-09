package com.assignment.service.impl;

import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.assignment.model.User;
import com.assignment.repository.UserRepository;
import com.assignment.service.UserService;

@Service
public class UserServiceImpl implements UserService {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;

	// Inject PasswordEncoder via constructor
	public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
	}

	@Override
	public User registerUsers(User user, String confirmPassword) {
		// Check if passwords match
		if (!user.getPassword().equals(confirmPassword)) {
			throw new RuntimeException("Password and Confirm Password not match!");
		}

		// Check if email already exists
		userRepository.findByEmailAddress(user.getEmailAddress()).ifPresent(u -> {
			throw new RuntimeException("Email already exists! Please use another email.");
		});

		// Encode the password before saving
		user.setPassword(passwordEncoder.encode(user.getPassword()));

		// Save user
		return userRepository.save(user);
	}

	@Override
	public Optional<User> forgetPassword(String email) {
		return userRepository.findByEmailAddress(email);
	}
}
