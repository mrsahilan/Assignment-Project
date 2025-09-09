package com.assignment.service;

import java.util.Optional;

import com.assignment.model.User;

public interface UserService 
{
	User registerUsers(User user);
	
	Optional<User> forgetPassword(String email);
}
