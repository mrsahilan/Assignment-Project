package com.assignment.service.impl;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.assignment.model.User;
import com.assignment.repository.UserRepository;
import com.assignment.service.UserService;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    
    public UserServiceImpl(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@Override
    public User registerUsers(User user) {
        // Use Optional directly from repository
        userRepository.findByEmailAddress(user.getEmailAddress())
                .ifPresent(u -> {
                    throw new RuntimeException("Email already exists! Please use another email.");
                });

        return userRepository.save(user);
    }

    @Override
    public Optional<User> forgetPassword(String email) {
        return userRepository.findByEmailAddress(email);
    }
}
