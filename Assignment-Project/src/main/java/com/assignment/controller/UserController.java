package com.assignment.controller;

import com.assignment.model.User;
import com.assignment.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    // Show Registration Page
    @GetMapping("/register")
    public String showRegisterPage(Model model) {
        model.addAttribute("user", new User());
        return "register";  // This refers to register.html inside templates
    }

    // Handle Form Submission
    @PostMapping("/register")
    public String registerUser(@ModelAttribute("user") User user, Model model) {
        try {
            userService.registerUsers(user);
            model.addAttribute("successMessage", "User registered successfully!");
            model.addAttribute("user", new User()); // Reset form fields
            return "register"; // Show the same page with success message
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Something went wrong! Please try again.");
            return "register";
        }
    }
}
