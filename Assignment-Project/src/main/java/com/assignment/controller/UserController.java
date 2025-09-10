package com.assignment.controller;

import java.util.Optional;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.assignment.model.User;
import com.assignment.service.UserService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/users")
public class UserController {

	private final UserService userService;

	public UserController(UserService userService) {
		this.userService = userService;
	}

	// SHOW REGISTRATION PAGE
	@GetMapping("/register")
	public String showRegisterPage(Model model) {
		model.addAttribute("user", new User());
		return "register"; // Loads register.html
	}

	// HANDLE USER REGISTRATION
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

	// SHOW LOGIN PAGE
	@GetMapping("/login")
	public String showLoginPage() {
		return "login"; // Loads login.html
	}

	// HANDLE USER LOGIN
	@PostMapping("/login")
	public String loginUser(@RequestParam("emailAddress") String email, @RequestParam("password") String password,
			HttpSession session, Model model) {
		try {
			Optional<User> loggedInUser = userService.loginUsers(email, password);

			if (loggedInUser.isPresent()) {
				// Store logged-in user in session
				session.setAttribute("loggedInUser", loggedInUser.get());

				// Redirect to dashboard or home page
				return "redirect:/users/dashboard";
			} else {
				model.addAttribute("errorMessage", "Invalid email or password!");
				return "login";
			}
		} catch (RuntimeException e) {
			model.addAttribute("errorMessage", e.getMessage());
			return "login";
		}
	}

	// DASHBOARD PAGE (AFTER LOGIN)
	@GetMapping("/dashboard")
	public String dashboard(HttpSession session, Model model) {
		User loggedInUser = (User) session.getAttribute("loggedInUser");

		if (loggedInUser == null) {
			return "redirect:/users/login"; // Redirect if not logged in
		}

		model.addAttribute("user", loggedInUser);
		return "dashboard"; // Loads dashboard.html
	}

	// SHOW RESET PASSWORD PAGE
	@GetMapping("/reset-password")
	public String showResetPasswordPage() {
		return "reset-password";
	}

	// HANDLE RESET PASSWORD - CHECK EMAIL
	@PostMapping("/reset-password")
	public String handleResetPassword(@RequestParam("emailAddress") String email, Model model, HttpSession session) {
		Optional<User> user = userService.forgetPassword(email);

		if (user.isPresent()) {
			// Store email in session for password change
			session.setAttribute("resetEmail", email);
			return "redirect:/users/change-password";
		} else {
			model.addAttribute("errorMessage", "Email not found!");
			return "reset-password";
		}
	}

	// SHOW CHANGE PASSWORD PAGE
	@GetMapping("/change-password")
	public String showChangePasswordPage() {
		return "change-password";
	}

	// HANDLE CHANGE PASSWORD
	@PostMapping("/change-password")
	public String handleChangePassword(@RequestParam("password") String password,
			@RequestParam("confirmPassword") String confirmPassword, HttpSession session, Model model) {

		String email = (String) session.getAttribute("resetEmail");

		if (email == null) {
			model.addAttribute("errorMessage", "Session expired! Please try again.");
			return "reset-password";
		}

		if (!password.equals(confirmPassword)) {
			model.addAttribute("errorMessage", "Password and Confirm Password do not match!");
			return "change-password";
		}

		try {
			userService.updatePassword(email, password);
			session.removeAttribute("resetEmail");
			model.addAttribute("successMessage", "Password updated successfully!");
			return "change-password";
		} catch (RuntimeException e) {
			model.addAttribute("errorMessage", e.getMessage());
			return "change-password";
		}
	}

	// USER LOGOUT METHOD 
	@PostMapping("/logout")
	public String logout(HttpSession session) {
		session.invalidate(); // Destroys the session
		return "redirect:/users/login?logout"; // Redirect to login page
	}
}
