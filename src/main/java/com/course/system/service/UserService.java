package com.course.system.service;

import com.course.system.model.Admin;
import com.course.system.model.Student;
import com.course.system.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private static final String FILE_NAME = "users.txt";

    @Autowired
    private FileService fileService;

    public void registerUser(User user) throws IOException {
        // Validation for duplicate username or email
        List<User> existingUsers = getAllUsers();
        for (User u : existingUsers) {
            if (u.getUsername().equalsIgnoreCase(user.getUsername())) {
                throw new IllegalArgumentException("Username already exists!");
            }
            if (u.getEmail().equalsIgnoreCase(user.getEmail())) {
                throw new IllegalArgumentException("Email already exists!");
            }
        }
        fileService.appendToFile(FILE_NAME, user.toString());
    }

    public List<User> getAllUsers() throws IOException {
        List<String> lines = fileService.readFromFile(FILE_NAME);
        List<User> users = new ArrayList<>();
        for (String line : lines) {
            String[] parts = line.split("\\|");
            if (parts.length >= 5) {
                String id = parts[0];
                String username = parts[1];
                String password = parts[2];
                String email = parts[3];
                String type = parts[4];

                if (type.equals("ADMIN")) {
                    users.add(new Admin(id, username, password, email));
                } else {
                    String degreeProgram = parts.length > 5 ? parts[5] : "General";
                    String fullName = parts.length > 6 ? parts[6] : username;
                    double gpa = parts.length > 7 ? Double.parseDouble(parts[7]) : 0.0;
                    int yearOfStudy = parts.length > 8 ? Integer.parseInt(parts[8]) : 1;
                    users.add(new Student(id, username, password, email, degreeProgram, fullName, gpa, yearOfStudy));
                }
            }
        }
        return users;
    }

    public Optional<User> getUserById(String id) throws IOException {
        return getAllUsers().stream().filter(u -> u.getId().equals(id)).findFirst();
    }

    public void updateUser(User updatedUser) throws IOException {
        List<User> users = getAllUsers();
        List<String> updatedLines = new ArrayList<>();
        for (User u : users) {
            if (u.getId().equals(updatedUser.getId())) {
                updatedLines.add(updatedUser.toString());
            } else {
                updatedLines.add(u.toString());
            }
        }
        fileService.writeToFile(FILE_NAME, updatedLines);
    }

    public void deleteUser(String id) throws IOException {
        List<User> users = getAllUsers();
        List<String> updatedLines = new ArrayList<>();
        for (User u : users) {
            if (!u.getId().equals(id)) {
                updatedLines.add(u.toString());
            }
        }
        fileService.writeToFile(FILE_NAME, updatedLines);
    }
}
