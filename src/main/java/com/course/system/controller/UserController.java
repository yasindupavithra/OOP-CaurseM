package com.course.system.controller;

import com.course.system.model.Admin;
import com.course.system.model.Student;
import com.course.system.model.User;
import com.course.system.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    // 1. Read (Search / View List UI)
    @GetMapping
    public String listUsers(@RequestParam(required = false) String search, Model model) throws IOException {
        List<User> list = userService.getAllUsers();

        if (search != null && !search.isEmpty()) {
            list = list.stream()
                    .filter(u -> u.getUsername().toLowerCase().contains(search.toLowerCase()) ||
                                 u.getEmail().toLowerCase().contains(search.toLowerCase()) ||
                                 u.getUserType().toLowerCase().contains(search.toLowerCase()))
                    .collect(Collectors.toList());
        }

        model.addAttribute("users", list);
        model.addAttribute("search", search);
        return "users/list";
    }

    // 2. Create (Add/Register User UI)
    @GetMapping("/register")
    public String showRegisterForm() {
        return "users/register";
    }

    @PostMapping("/register")
    public String register(@RequestParam String username, @RequestParam String password,
                           @RequestParam String email, @RequestParam String type,
                           @RequestParam(required = false) String program, Model model) {
        
        // Validation checks
        if (username.trim().isEmpty() || password.trim().isEmpty() || email.trim().isEmpty()) {
            model.addAttribute("error", "All fields are required!");
            return "users/register";
        }
        if (!email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            model.addAttribute("error", "Invalid email format!");
            return "users/register";
        }

        String id = "usr_" + UUID.randomUUID().toString().substring(0, 5);
        User user;
        if (type.equals("ADMIN")) {
            user = new Admin(id, username, password, email);
        } else {
            user = new Student(id, username, password, email, program, username, 0.0, 1);
        }
        
        try {
            userService.registerUser(user);
            return "redirect:/users";
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            return "users/register";
        } catch (IOException e) {
            model.addAttribute("error", "Failed to write user to database file!");
            return "users/register";
        }
    }

    // 3. Update (Edit User UI)
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable String id, Model model) throws IOException {
        userService.getUserById(id).ifPresent(u -> model.addAttribute("user", u));
        return "users/edit";
    }

    @PostMapping("/edit")
    public String editUser(@RequestParam String id, @RequestParam String username,
                           @RequestParam String password, @RequestParam String email,
                           @RequestParam String type, @RequestParam(required = false) String program, Model model) throws IOException {
        
        if (username.trim().isEmpty() || password.trim().isEmpty() || email.trim().isEmpty()) {
            model.addAttribute("error", "All fields are required!");
            userService.getUserById(id).ifPresent(u -> model.addAttribute("user", u));
            return "users/edit";
        }
        if (!email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            model.addAttribute("error", "Invalid email format!");
            userService.getUserById(id).ifPresent(u -> model.addAttribute("user", u));
            return "users/edit";
        }

        User updated;
        if (type.equals("ADMIN")) {
            updated = new Admin(id, username, password, email);
        } else {
            updated = new Student(id, username, password, email, program, username, 0.0, 1);
        }

        try {
            userService.updateUser(updated);
            return "redirect:/users";
        } catch (IOException e) {
            model.addAttribute("error", "Failed to update record!");
            model.addAttribute("user", updated);
            return "users/edit";
        }
    }

    // 4. Delete (Remove UI/Page with Confirmation)
    @GetMapping("/delete/{id}")
    public String showDeleteConfirmation(@PathVariable String id, Model model) throws IOException {
        userService.getUserById(id).ifPresent(u -> model.addAttribute("user", u));
        return "users/delete";
    }

    @PostMapping("/delete")
    public String deleteUser(@RequestParam String id) throws IOException {
        userService.deleteUser(id);
        return "redirect:/users";
    }
}
