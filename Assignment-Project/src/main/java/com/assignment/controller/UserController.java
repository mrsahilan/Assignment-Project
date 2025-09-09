package com.assignment.controller;

import com.assignment.model.User;
import com.assignment.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/users")
public class UserController {

	private final UserService userService;

	public UserController(UserService userService) {
		this.userService = userService;
	}

	// Show Registration Page
	@GetMapping("/register")
	public String showRegisterPage(Model model) {
		model.addAttribute("user", new User());
		return "register"; // This refers to register.html inside templates
	}

	// Handle Form Submission
	@PostMapping("/register")
	public String registerUser(@ModelAttribute("user") User user,
			@RequestParam("confirmPassword") String confirmPassword, Model model) {
		try {
			userService.registerUsers(user, confirmPassword);
			model.addAttribute("successMessage", "User registered successfully!");
			model.addAttribute("user", new User()); // Reset form
		} catch (RuntimeException e) {
			model.addAttribute("errorMessage", e.getMessage());
		}
		return "register";
	}
}
