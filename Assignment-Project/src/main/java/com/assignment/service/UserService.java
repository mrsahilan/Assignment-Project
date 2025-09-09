package com.assignment.service;

import java.util.Optional;

import com.assignment.model.User;

public interface UserService 
{
	User registerUsers(User user, String confirmPassword);
	
	Optional<User> forgetPassword(String email);
	
	Optional<User> loginUsers(String email, String password);
}
