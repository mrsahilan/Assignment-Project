package com.assignment.controller;

import com.assignment.model.User;
import com.assignment.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

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
                               @RequestParam("confirmPassword") String confirmPassword,
                               Model model) {
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
    public String loginUser(@RequestParam("emailAddress") String email,
                            @RequestParam("password") String password,
                            HttpSession session,
                            Model model) {
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
}
