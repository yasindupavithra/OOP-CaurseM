package com.course.system.controller;

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

/**
 * StudentController handles all CRUD operations and validations
 * related to Student Profile Management inside the EduReg system.
 * 
 * Major features:
 * - GPA Boundary Checks (0.0 - 4.0)
 * - Year of Study limits (1 - 5)
 * - Safe flat-file mapping
 * 
 * @author Member 01 (You)
 */
@Controller
@RequestMapping("/students")
public class StudentController {

    @Autowired
    private UserService userService;

    /**
     * Retrieves all student accounts from file database and filters them based on query string.
     */
    @GetMapping
    public String listStudents(@RequestParam(required = false) String search, Model model) throws IOException {
        List<User> students = userService.getAllUsers().stream()
                .filter(u -> u.getUserType().equals("STUDENT"))
                .collect(Collectors.toList());

        if (search != null && !search.isEmpty()) {
            students = students.stream()
                    .filter(u -> ((Student) u).getFullName().toLowerCase().contains(search.toLowerCase()) ||
                                 u.getUsername().toLowerCase().contains(search.toLowerCase()) ||
                                 ((Student) u).getDegreeProgram().toLowerCase().contains(search.toLowerCase()))
                    .collect(Collectors.toList());
        }

        model.addAttribute("students", students);
        model.addAttribute("search", search);
        return "students/list";
    }

    // 2. Create (Add Student UI)
    @GetMapping("/add")
    public String showAddForm() {
        return "students/add";
    }

    @PostMapping("/add")
    public String addStudent(@RequestParam String username, @RequestParam String password,
                             @RequestParam String email, @RequestParam String fullName,
                             @RequestParam String degreeProgram, @RequestParam double gpa,
                             @RequestParam int yearOfStudy, Model model) {
        // Validation checks
        if (username.trim().isEmpty() || password.trim().isEmpty() || email.trim().isEmpty() || fullName.trim().isEmpty()) {
            model.addAttribute("error", "All fields are required!");
            return "students/add";
        }
        if (!email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            model.addAttribute("error", "Invalid email format!");
            return "students/add";
        }
        if (gpa < 0.0 || gpa > 4.0) {
            model.addAttribute("error", "GPA must be between 0.0 and 4.0!");
            return "students/add";
        }
        if (yearOfStudy < 1 || yearOfStudy > 5) {
            model.addAttribute("error", "Year of study must be between 1 and 5!");
            return "students/add";
        }

        String id = "std_" + UUID.randomUUID().toString().substring(0, 5);
        Student student = new Student(id, username, password, email, degreeProgram, fullName, gpa, yearOfStudy);

        try {
            userService.registerUser(student);
            return "redirect:/students";
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            return "students/add";
        } catch (IOException e) {
            model.addAttribute("error", "Failed to save student to file database!");
            return "students/add";
        }
    }

    // 3. Update (Edit Student UI)
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable String id, Model model) throws IOException {
        userService.getUserById(id).ifPresent(u -> model.addAttribute("student", u));
        return "students/edit";
    }

    @PostMapping("/edit")
    public String editStudent(@RequestParam String id, @RequestParam String username,
                              @RequestParam String password, @RequestParam String email,
                              @RequestParam String fullName, @RequestParam String degreeProgram,
                              @RequestParam double gpa, @RequestParam int yearOfStudy, Model model) throws IOException {
        // Validation checks
        if (username.trim().isEmpty() || password.trim().isEmpty() || email.trim().isEmpty() || fullName.trim().isEmpty()) {
            model.addAttribute("error", "All fields are required!");
            userService.getUserById(id).ifPresent(u -> model.addAttribute("student", u));
            return "students/edit";
        }
        if (!email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            model.addAttribute("error", "Invalid email format!");
            userService.getUserById(id).ifPresent(u -> model.addAttribute("student", u));
            return "students/edit";
        }
        if (gpa < 0.0 || gpa > 4.0) {
            model.addAttribute("error", "GPA must be between 0.0 and 4.0!");
            userService.getUserById(id).ifPresent(u -> model.addAttribute("student", u));
            return "students/edit";
        }

        Student updated = new Student(id, username, password, email, degreeProgram, fullName, gpa, yearOfStudy);
        try {
            userService.updateUser(updated);
            return "redirect:/students";
        } catch (IOException e) {
            model.addAttribute("error", "Failed to update record!");
            model.addAttribute("student", updated);
            return "students/edit";
        }
    }

    // 4. Delete (Remove UI/Page with Confirmation)
    @GetMapping("/delete/{id}")
    public String showDeleteConfirmation(@PathVariable String id, Model model) throws IOException {
        userService.getUserById(id).ifPresent(u -> model.addAttribute("student", u));
        return "students/delete";
    }

    @PostMapping("/delete")
    public String deleteStudent(@RequestParam String id) throws IOException {
        userService.deleteUser(id);
        return "redirect:/students";
    }
}
